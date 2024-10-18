# 🪄 Trello Project

프로젝트를 편리하고 효율적으로 관리할 수 있는 협업 서비스

# 🚀 STACK

Environment

![인텔리제이](   https://img.shields.io/badge/IntelliJ_IDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)
![](https://img.shields.io/badge/Gradle-02303a?style=for-the-badge&logo=gradle&logoColor=white)
![](https://img.shields.io/badge/Postman-ff6c37?style=for-the-badge&logo=postman&logoColor=white)
![깃허브](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)
![깃이그노어](https://img.shields.io/badge/gitignore.io-204ECF?style=for-the-badge&logo=gitignore.io&logoColor=white)
![깃](https://img.shields.io/badge/GIT-E44C30?style=for-the-badge&logo=git&logoColor=white)

Development

![스프링부트](https://img.shields.io/badge/SpringBoot-6db33f?style=for-the-badge&logo=springboot&logoColor=white)
![자바](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)

Communication

![슬랙](  https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=slack&logoColor=white)
![노션](https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white)

# 🏗️프로젝트 설계

### [Wireframe](https://drive.google.com/file/d/13m5Vp9zGGGREIl5vuUUuUYyUdVVzDHqm/view?usp=sharing)

### [API Document](https://documenter.getpostman.com/view/37564576/2sAXxTcqm9)

### [ERD diagram](https://www.erdcloud.com/d/s5i7PGFn8pqCuMkPj)

# 🔍 프로젝트 기능 정리

SignUp / SignIn

    이메일, 비밀번호, UserRole을 입력하여 회원가입을 진행합니다.
    이미 가입되어있는 이메일로는 가입할 수 없습니다.
    회원가입 시 입력한 이메일과 비밀번호를 입력하여 로그인합니다.

Workspace
    
    여러 사용자들이 모여 협업을 진행하는 공간입니다.
    이름과 설명을 입력하여 생성합니다.
    Workspace는 여러개의 Board를 포함할 수 있습니다.
    ADMIN 사용자만 새로운 Workspace를 생성할 수 있습니다.
    Workspace에 초대되는 사용자들은 'WORKSPCE', 'BOARD', 'READ_ONLY' 세가지 중 하나의 WorkspaceRole을 부여 받습니다.
    해당 Workspace의 'WORKSPCE' 권한을 가진 사용자는 Workspace를 수정, 삭제하고 새로운 사용자를 초대할 수 있습니다.
    Workspace를 삭제하면 관련된 Board, List, Card, Comment가 모두 삭제됩니다.
    모든 사용자는 자신이 속한 Workspace를 다건, 단건 조회할 수 있습니다.

Board

    Workspace에 포함된 하나의 큰 작업 공간입니다.
    이름과 배경 또는 이미지를 입력하여 생성합니다.
    Board는 여러 개의 List를 포함할 수 있습니다.
    Board를 생성, 수정, 삭제 하려면 대상 Workspace의 'WORKSPACE', 'BOARD' 권한을 가지고 있어야 합니다.
    Board를 삭제하면 관련된 List, Card, Comment가 모두 삭제됩니다.
    Board가 포함된 Workspace에 속한 사용자만 조회할 수 있습니다.
    
List

    Board에 포함된 특정 상태나 카테고리 별 분류입니다.
    이름을 입력하여 생성합니다.
    List는 여러 개의 Card를 포함할 수 있습니다.
    List를 생성, 수정, 삭제 하려면 권한이 필요합니다.
    권한을 가지고 있는 사용자는 Board 내에서 List의 순서를 변경할 수 있습니다.
    List를 삭제하면 관련된 하위 데이터가 모두 삭제됩니다.
    List가 포함된 workspace의 사용자만 조회할 수 있습니다.

Card

    List에 포함된 가장 작은 작업단위 입니다.
    이름, 설명, 마감일, 담당자를 입력하여 생성합니다.
    Card는 여러 개의 Comment를 포함할 수 있습니다.
    Card를 생성, 수정, 삭제 하려면 권한이 필요합니다.
    권한을 가지고 있는 사용자는 List 내에서 Card의 순서를 변경하거나 다른 List로 이동할 수 있습니다.
    Card를 삭제하면 관련된 하위 데이터가 모두 삭제됩니다.
    Card가 포함된 Wrokspace의 사용자만 조회할 수 있습니다.

Comment
    
    권한을 가지고 있는 사용자는 Card에 Comment를 작성할 수 있습니다.
    Comment 내용을 입력하여 생성하고, emoji를 추가할 수 있습니다.
    Comment가 포함된 Workspace의 사용자만 조회할 수 있습니다.
    수정, 삭제는 Comment를 작성한 사용자만 가능합니다.

# ⚒️트러블슈팅

## QClass 파일 미생성으로 인한 컴파일 에러

- 문제 상황
  - QClass 파일이 없는 상태에서 팀원들의 코드를 로컬로 pull 받아 프로젝트를 실행하려 했을 때, 컴파일 에러가 발생
- 원인 분석
  - QueryDSL은 코드에 정의된 엔티티 클래스를 바탕으로 자동으로 QClass 파일을 생성하는데 commit 기록에 다른 팀원이 사용한 QClass 파일이 포함되지 않은 상황
  - QClass가 필요한 코드 부분에서 컴파일 에러가 발생
- 해결 방법
  - QClass 파일을 필요로 하는 코드 부분을 주석 처리하고 프로젝트를 컴파일하여 QueryDSL 실행으로 QClass 자동 생성
  - QClass 파일을 생성 후 코드 주석 처리를 해제한 후 프로젝트를 다시 컴파일하여 정상적으로 실행

## Slack 알람에서 사용자 멘션

- 문제 상황
  - 특정 메소드 호출 완료 후 Slack 알람을 보내는 과정에서 사용자가 멘션되지 않고 Slack ID 값이 그대로 출력되는 문제 발생
- 원인 분석
  - 임시 데이터를 넣은 상태의 DB를 기준으로 발급받은 JWT 토큰을 사용하여 Slack 알람에서 사용자 멘션 기능이 정상적으로 작동하지 않음
- 해결 방법
  - 새로운 JWT 토큰을 발급받아 올바른 사용자 정보가 포함되도록 수정하고, 이를 사용하여 Slack 알람에서 사용자 멘션이 정상적으로 동작하는 것을 확인

## 캐싱 어뷰징 방지
- 문제 상황
  - 조회수가 중복으로 적용되어 동일한 사용자가 계속해서 조회수를 올릴 수 있었음
- 원인 분석
  - 조회수 카운팅 기능을 구현하는 데 분별 없이 그냥 +1로 처리함
- 해결 방법
  - Duration을 사용해 하루만 키가 유지되게 하였고, 그 후 set() 메서드를 사용해 데이터를 저장하는 식으로 해결함

## 동시성 제어 테스트 기본 구성 클래스 감지 실패

- 문제 상황
  - 여러 멤버가 동시에 카드를 업데이트할 때 데이터의 일관성과 무결성을 유지하기 위해 동시성 제어를 활용하고 싶었으나 코드로 구현해내지 못함
- 원인 분석
  - 에러 메세지 확인결과 테스트 클래스에 대한 기본 구성 클래스를 감지할 수 없다는 메세지 확인
- 해결 방법
  - ContextConfiguration 어노테이션으로 테스트에 사용할 특정 구성 클래스를 명시하여 해당 문제 해결
   
## 예외처리
- 문제 상황
  - 사용자가 카드를 삭제하려고 할 때, 읽기 전용 권한을 가진 경우 예외가 발생해야하나 이 로직을 작성하면서 어떤 정보를 어떻게 추적해야 할지 혼란스러웠다.
- 원인 분석
  - 예외처리 읽기모드인지 구분하기 위해서는 상위의 리스트, 보드, 워크스페이스를 찾아내서 현재 로그인한 사용자를 찾아 권한확인을 해야하는 상황
- 해결 방법
  - 팀원분에게 각 단계에서 어떻게 해야하는 지 여쭤보고, 사용자 권한 로직 구현
    -> 사용자 권한을 확인하는 조건문을 작성할 때, 역할(Enum)의 값을 비교함 WorkspaceUserRole.READ_ONLY와 비교하여 조건이 충족되는 경우 적절한 예외를 발생시킴

## 더미데이터 추가하기
- 문제 상황
  - Jmeter를 사용하여 더미데이터를 추가하려는 중에 400에러가 뜨며 해결이 되지 않았다.
- 원인 분석
  - 튜터님을 찾아가 서버와 응답을 받는지 확인하고, 서버의 동작을 확인함. 서버 문제가 아님을 확인한 뒤, 요청값들을 확인했음
- 해결 방법
  - 요청값에 들어가있는 헤더의 공백한줄이 문제가 됐었던 걸 확인, 삭제 후 돌리니 더미값이 제대로 들어감을 확인함.

[추가기능] 
- https://velog.io/@uara67/트렐로인덱싱하여속도측정하기
  인덱싱 속도 비교
- https://everyday-spring.com/623 
Spring Boot AOP사용하여 Slack 사용자 멘션하기
- https://everyday-spring.com/622
SpringBoot에서 첨부파일 처리하기 AWS S3
