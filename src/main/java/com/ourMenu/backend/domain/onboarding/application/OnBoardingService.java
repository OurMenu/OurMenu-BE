package com.ourMenu.backend.domain.onboarding.application;

import com.ourMenu.backend.domain.menu.application.MenuService;
import com.ourMenu.backend.domain.menu.dao.MenuRepository;
import com.ourMenu.backend.domain.menu.domain.Menu;
import com.ourMenu.backend.domain.menu.dto.MenuIdentifier;
import com.ourMenu.backend.domain.onboarding.dao.OnBoardingStateRepository;
import com.ourMenu.backend.domain.onboarding.domain.AnswerType;
import com.ourMenu.backend.domain.onboarding.domain.DefaultTag;
import com.ourMenu.backend.domain.onboarding.domain.OnBoardingState;
import com.ourMenu.backend.domain.onboarding.domain.Question;
import com.ourMenu.backend.domain.onboarding.exception.SearchResultNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class OnBoardingService {

    private final MenuRepository menuRepository;
    private final MenuService menuService;
    private final OnBoardingStateRepository onBoardingStateRepository;

    public List<Question> getAllQuestion() {
        return Question.getAllQuestions();
    }

    @Transactional
    public List<Menu> saveAndFindStoreByQuestionAnswer(Long userId, int questionId, AnswerType answerType) {
        OnBoardingState onBoardingState = saveAndUpdateOnBoardingState(userId, questionId, answerType);
        return findStoreByQuestionAnswer(userId, questionId, answerType);
    }

    @Transactional
    public OnBoardingState saveAndUpdateOnBoardingState(Long userId, int questionId, AnswerType answerType){
        Optional<OnBoardingState> onBoardingStateOptional = onBoardingStateRepository.findByUserId(userId);
        if(onBoardingStateOptional.isEmpty()){
            OnBoardingState onBoardingState = OnBoardingState.toEntity(userId, questionId, answerType);
            return save(onBoardingState);
        }
        OnBoardingState onBoardingState = onBoardingStateOptional.get();
        onBoardingState.update(questionId,answerType);
        return onBoardingState;
    }

    @Transactional
    public List<Menu> findStoreByQuestionAnswer(Long userId, int questionId, AnswerType answerType) {
        List<String> foodStringList = Question.getAnswerFoodByIdAndAnswerType(questionId, answerType);
        Map<Long, Menu> map = new HashMap<>();
        for (String s : foodStringList) {
            List<Menu> menus = menuRepository.findMenusByTitleContainingAndUserId(s, userId);
            for (Menu menu : menus) {
                map.put(menu.getGroupId(), menu);
            }
        }

        return map.values().stream().toList();
    }

    /**
     * questionId와 answerType에 해당하는 메뉴를 가진 다른사람(본인의 것은 제외한다)의 유저의 메뉴를 가져온다
     * @param userId
     * @param questionId
     * @param answerType
     * @return
     */
    @Transactional
    public List<Menu> findOtherUserMenusByQuestionAnswer(Long userId, int questionId, AnswerType answerType) {
        List<String> foodStringList = Question.getAnswerFoodByIdAndAnswerType(questionId, answerType);
        List<Menu> menuList = new ArrayList<>();

        for (String foodString : foodStringList) {
            List<Menu> menus = menuRepository.findMenusByTitleContaining(foodString);
            menuList.addAll(menus);
        }

        Map<MenuIdentifier,Menu> map = new HashMap<>();

        for (Menu menu : menuList) {
            if(menu.getUser().getId() == userId)
                continue;
            MenuIdentifier menuIdentifier = new MenuIdentifier(menu.getGroupId(),menu.getUser().getId());
            map.put(menuIdentifier,menu);
        }

        return map.values().stream().toList();
    }


    public List<Menu> findStoreByRandomTag(Long userId, DefaultTag randomTag) {
        return menuService.getAllMenusByTagName(randomTag.getTagName(), userId);
    }

    public List<Menu> findOtherStoreByRandomTag(Long userId, DefaultTag randomTag) {
        List<Menu> menuList = menuService.getAllOtherMenusByTagName(randomTag.getTagName(), userId);
        Map<MenuIdentifier,Menu> map = new HashMap<>();
        for (Menu menu : menuList) {
            if(menu.getUser().getId() == userId)
                continue;
            MenuIdentifier menuIdentifier = new MenuIdentifier(menu.getGroupId(),menu.getUser().getId());
            map.put(menuIdentifier,menu);
        }
        return map.values().stream().toList();
    }

    public List<Menu> findOtherUserStoreByRandomTag(Long userId, DefaultTag randomTag){
        return menuService.getAllMenusByTagNameAndUserIdNot(randomTag.getTagName(), userId);
    }

    @Transactional(readOnly = true)
    public String findOnboardingStateByUserId(Long userId) {
        OnBoardingState onBoardingState = findOneById(userId);

        return "success";
    }

    @Transactional
    public OnBoardingState findOneById(Long userId) {
        Optional<OnBoardingState> onBoardingStateOptional = onBoardingStateRepository.findByUserId(userId);
        if (onBoardingStateOptional.isEmpty())
            throw new SearchResultNotFoundException();
        return onBoardingStateOptional.get();
    }

    @Transactional
    public OnBoardingState save(OnBoardingState onBoardingState) {
        return onBoardingStateRepository.save(onBoardingState);
    }


}
