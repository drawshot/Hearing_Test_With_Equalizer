## Hearing_Test_With_Equalizer

### 프로젝트 기간
2017-08-01 ~ 2017-12-31

### 프로젝트 설명
난청이란 작은 소리를 듣지 못하거나 들리는 소리를 구분할 수 없는 상태를 말한다.
최근 고령화 사회 직업과 레져 등 다양한 원인으로 인한 소음 노출 증가로 인해 난청 환자들이 매년 증가하고 있는 추세이며, 이로 인해 정학환 청력검사, 난청 예방 및 치료 등에 대한 관심이 높아지고 있다.
본 프로젝트는 이러한 난청 환자들을 위한 청력검사 앱 제작과 개인의 청력상태에 맞게 이퀄라이져를 설정하는 것을 목표로 한다.
Hearable 디바이스는 개인 청력 상태에 대한 양측 주파수별 보정으로 정확한 음의 청취 기능을 제공.
<br />
<img width="743" alt="스크린샷 2019-09-21 오후 4 12 14" src="https://user-images.githubusercontent.com/26424846/65369642-a8f90380-dc8a-11e9-8bcf-29b14c5327ea.png">
<br />
<br />
순음(Pure Tone)을 발생시키기 위해서 먼저 Sine Wave의 PCM Data를 만든다.
<br />
발생하려고 하는 순음의 주파수는 44K의 샘플율(Sample Rate)과 Duration을 통해서 결정이 되며, 순음의 음압레벨은 음압레벨-Amp 테이블을 통해서 얻어진 Amp값을 이용한다.
<br />
이를 이용해 해당하는 구간의 PCM 데이터를 생성한 후 Audio Buffer에 저장.
<br />
이때 좌우 데이터는 각각 16bit씩 교차하면서 저장.
<br />
순음을 발생할 때는 Audio Buffer에 저장된 데이터를 Audio Track의 인스턴스로 전송.
<br />
전송된 데이터는 Audio Mixer를 거치는데, 이때 순음 재생이 Main Thread에서 처리할 경우 재생이 진행되는 동안 사용자 인터렉션을 처리할 수 없으므로 Main Thread외에 Thread를 별도 생성해서 재생.
<br />
이후에 HAL(Hardware Abstraction Layer)를 거쳐 순음 재생.
<br />
<br />
<img width="601" alt="스크린샷 2019-09-21 오후 4 19 58" src="https://user-images.githubusercontent.com/26424846/65369716-bd89cb80-dc8b-11e9-90f7-6ed1aefc2600.png">
<br />
<br />
청력검사 알고리즘은 위 그림과 같으며, 주파수의 순서는 1000Hz, 2000Hz, 3000Hz, 4000Hz, 8000Hz, 1000Hz, 500Hz, 250Hz, 125Hz 순으로 시행
<br />
하강법과 상승법이 섞인 혼합법(Mixed method)로 청력검사 실시
* 정상적인 청력을 가진 평균의 인간이 들을 수 있는 소리인 30dB HL(최소가청역치)를 검사 기준으로 삼고 오차 값을 더하여 데시벨을 출력
* 출력된 검사음에 응답하였을 경우에는 10dB를 감소하여 다음 검사 음을 출력하고, 응답하지 않았을 경우에는 20dB를 증가하여 다음 검사음 출력
* 첫 응답 이후 두 번째 응답부터는 응답부재의 경우 5dB을 증가시키면서 세밀하게 검사 진행
* 3번 응답했을 경우에는 그 값을 역치로 저장하고 다음 주파수로 진행
* 현재 검사 중인 주파수가 배열의 마지막인 125Hz일 경우에는 검사를 완료하고, 125Hz가 아닐 경우에는 다음 주파수로 진행
<br />
<img width="722" alt="스크린샷 2019-09-24 오후 5 22 54" src="https://user-images.githubusercontent.com/26424846/65494545-0215a100-def0-11e9-910f-e636273fb0b7.png">
<br />
스마트폰 앱과 블루투스 장치로 구성<br />
* 청력검사 결과를 Bluetooth LE를 통해 장치로 전송<br />
* 장치는 전송되어온 청력검사결과를 분석해서 Equalizer Controller를 통해 Equalizer의 각 주파수 밴드(좌우 각각 12 band)의 gain 값을 조정<br />
* 청력검사 결과의 해당 주파수의 청력 역치가 높은 경우 gain 값(0~12dB)을 증가<br />
* 역치가 낮을 경우는 반대로 gain 값(0~-12dB)을 감소시켜서 전체적으로 균일한 소리를 느낄 수 있게 동작<br />
한번 설정된 gain 값들은 스마트폰을 통해서 다시 설정할 때까지 유지<br />

### 개발 환경
* 위 앱은 Android Studio 3.1 버전에서 제작
* 안드로이드 롤리팝 이상 지원

### 사용 예제
![equalizer_1](https://user-images.githubusercontent.com/26424846/65661138-ca336880-e06b-11e9-835e-b4e839e1a0ad.jpg)
![equalizer_2](https://user-images.githubusercontent.com/26424846/65609196-54d38380-dfea-11e9-8c75-f9b2cd9133b1.jpg)
![equalizer_3](https://user-images.githubusercontent.com/26424846/65609220-5f8e1880-dfea-11e9-91a6-1588088da850.jpg)
