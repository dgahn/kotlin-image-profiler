# Kotlin Image Profiler

## 개요

이 프로젝트는 [Image Profiler](https://github.com/dgahn/image-profiler)의 Kotlin 버전입니다.

<br>

## 개발 환경

- IDE : IntelliJ IDEA Ultimate
- OS : Mac OS X
- Kotlin 1.3.60
- Java 11.0.1
- Akka HTTP 10.1.10
- Akka 2.6.0
- Hibernate 5.4.8.Final
- Spek 2.0.8


<br>

## 실행 방법

- bash shell 

```bash
$ ./gradlew tasks
$ ./gradlew build
$ ./gradlew run
```

- power shell

```power
PS> gradlew tasks
PS> gradlew build
PS> gradlew run
```

<br>

## API Test With Postman

- [Postman Download](https://www.getpostman.com/downloads/) 참고하여 postman을 설치합니다. 

<br>

- Postman을 실행하여 왼쪽 상단의 **Import**를 선택합니다.

![](https://imgur.com/JqQuw1J.png)

<br>

- 프로젝트 루트에서 **Postman Script**인 **postman/Image Profiler TEST.postman_collection.json** 파일을 확인하고 **Postman Script**를 드래그로 삽입하거나 **Open file** 버튼을 통해서 선택합니다.

![](https://imgur.com/1RkbPaD.png)

<br>

- **Postman Script**가 삽입되면 **Image Profiler TEST** collection을 선택한 후 **생성** Request를 선택합니다. 그리고 이미지를 선택하기 위해 **Body**를 선택합니다.

![](https://imgur.com/k0AIlxX.png)

<br>

- **이미지**를 업로드하기 위해 **Select File**을 버튼을 선택하고 이미지를 선택합니다. 파일 형식은 EXIF를 지원하는 파일이어야합니다. (참고: [교환 이미지 파일 형식](https://ko.wikipedia.org/wiki/%EA%B5%90%ED%99%98_%EC%9D%B4%EB%AF%B8%EC%A7%80_%ED%8C%8C%EC%9D%BC_%ED%98%95%EC%8B%9D), 프로젝트 루트에 postman 폴더에 예시 이미지가 있습니다.) 추가적으로 **Key**가 **name**인 **Value**에 이미지의 이름을 삽입합니다.

![](https://imgur.com/cMYH0kt.png)

<br>

- **Send** 버튼을 통해서 이미지 이름과 이미지를 전송하고 profile 생성을 요청API를 확인합니다.

![](https://imgur.com/oHApQYF.png)

<br>

- **목록 조회** Request를 선택하여 profile의 목록을 조회합니다.

![](https://imgur.com/vVkHKP4.png)

<br>

- **id로 조회** Request를 선택하여 지정한 id의 profile 내용을 조회합니다.

![](https://imgur.com/BKib3wl.png)