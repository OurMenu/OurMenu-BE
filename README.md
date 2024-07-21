## OurMenu backend 
### git convention
| 태그이름       | 내용                                          |
|------------|---------------------------------------------|
| `feat`     | 새로운 기능을 추가할 경우                              |
| `fix `     | 버그를 고친 경우                                   |
| `style`    | 코드 포맷 변경, 세미 콜론 누락, 코드 수정이 없는 경우            |
| `refactor` | 코드 리팩토링                                     |
| `comment`  | 필요한 주석 추가 및 변경                              |
| `docs`	    | 문서, Swagger 를 수정한 경우                        |
| `test`     | 테스트 추가, 테스트 리팩토링(프로덕션 코드 변경 X)              |
| `chore`	   | 빌드 태스트 업데이트, 패키지 매니저를 설정하는 경우(프로덕션 코드 변경 X) |
| `rename`   | 파일 혹은 폴더명을 수정하거나 옮기는 작업만인 경우                |
| `remove`   | 파일을 삭제하는 작업만 수행한 경우                         |
### 

### review
- PR은 코드 변경이 없는 경우를 제외하고 리뷰와 승인을 필요로 합니다
- PR는 build가 성공되는 경우에 merge 할 수 있도록 합니다.
- main는 최종, develop은 개발 branch이고, 브랜치명으로 해당 작업을 명세합니다 ex) feat/deploy
- [패키지를 도메인별로 분리하는 작업합니다](https://velog.io/@jsb100800/Spring-boot-directory-package)
- 
