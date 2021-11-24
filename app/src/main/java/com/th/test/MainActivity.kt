package com.th.test

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import java.lang.Math.abs
import java.util.*


class MainActivity : AppCompatActivity() {

    // 참가자 수
    var p_num = 3
    // 참가자의 번호
    var k = 1
    // 참가자들의 점수 리스트
    val point_list = mutableListOf<Float>()
    // 블라인드 모드
    var isBlind = true

    // activity_start.xml 실행 함수
    fun start(){
        setContentView(R.layout.activity_start)
        val tv_pnum : TextView = findViewById(R.id.tv_random)
        val btn_m : Button = findViewById(R.id.btn_minus)
        val btn_p : Button = findViewById(R.id.btn_plus)
        val btn : Button = findViewById(R.id.btn_start)

        tv_pnum.text = p_num.toString()

        // - 버튼을 누르면 일어나는 일
        btn_m.setOnClickListener{
            // 숫자가 감소
            p_num --
            // 숫자가 1 미만으로 내려가지 않음
            if(p_num == 0) {
                p_num = 1
            }
            tv_pnum.text = p_num.toString()
        }
        // + 버튼을 누르면 일어나는 일
        btn_p.setOnClickListener{
            p_num ++
            // 숫자가 4 이상으로 올라가지 않음음
           if(p_num == 10) {
                p_num = 9
            }
            tv_pnum.text = p_num.toString()
        }
        // 시작 버튼을 누르면 main() 함수가 출력
        btn.setOnClickListener{
            main()
        }
    }

    // activity_main.xml 실행 함수
    fun main(){
        setContentView(R.layout.activity_main)

        var timerTask : Timer? = null
        var stage = 1
        var sec : Int = 0
        val tv_pnum : TextView = findViewById(R.id.tv_random)
        val tv_t : TextView = findViewById(R.id.tv_timer)
        val tv_p : TextView = findViewById(R.id.tv_point)
        val tv_people : TextView = findViewById(R.id.tv_people)
        val btn : Button = findViewById(R.id.btn_start)
        // 버튼을 클릭하면 1~10 사이에 숫자 중 랜덤한 숫자 출력
        val random_box = Random()
        val num = random_box.nextInt(1001)

        // 목표 숫자 출력
        tv_pnum.text = (num.toFloat()/100).toString()
        btn.text = "시작"
        tv_people.text = "참가자 $k"

        // setOnClickListener -> 버튼 클릭을 하면 돌아오는 반응을 만듦
        // 랜덤한 숫자에 최대한 가까이 점수를 맞춘다.
        btn.setOnClickListener{

            // false -> true => false일 때는 if 문이 돌지 않고 true일 때는 돌아간다.
            stage ++
            if(stage == 2) {
                // 1000 - 1초 / 100 / 0.1초 ..
                timerTask = kotlin.concurrent.timer(period = 10) {
                    sec++
                    runOnUiThread {
                        // 블라인드 모드가 꺼져있을때
                        if(isBlind == false) {
                            // 100분의 1로 나눈 int sec는 소수점이 나올 수 없으므로
                            // double이나 float 타입으로 캐스팅해서 출력
                            tv_t.text = (sec.toDouble() / 100).toString()
                        // 블라인드 모드가 켜져있을때
                                // 블라인드 모드가 켜져있고 스테이지는 2단계
                        } else if (isBlind == true && stage == 2){
                            tv_t.text = " ?.??" // 시간이 보이지 않음
                        }
                    }
                }
                btn.text = "정지"
            } else if (stage == 3) {
                // 블라인드 모드이더라도 정지를 누르면 시간이 나오도록 처리
                tv_t.text = (sec.toDouble() / 100).toString()
                timerTask?.cancel()
                // 랜덤값과 버튼을 눌러서 나온 값의 차이를 출력
                val point = abs(sec - num).toFloat() / 100

                // 참가자들의 점수를 리스트에 추가가
                point_list.add(point)

                tv_p.text = point.toString()
                btn.text = "다음"
                stage = 0
            }else if(k<p_num){
                val point = abs(sec - num).toFloat() / 100
                    tv_p.text = point.toString()
                    btn.text = "다음"
                    k++
//                    stage = 0
                    main()
                     // 다음이 출력되는 stage 3 때 처음으로 돌아가도록 stage=0으로 설정
            } else {
                val point = abs(sec - num).toFloat() / 100
                tv_p.text = point.toString()
                btn.text = "결과"
                btn.setOnClickListener{
                    end()
                }
            }
//                if(k>=p_num){
//                                        btn.text = "결과"
//                    btn.setOnClickListener{
//                        end()
//                    }
//                }
//                tv_p.text = point.toString()
//                btn.text = "다음"
//                k++
//                stage = 0
//                main()
//            }
//            else if (stage == 1){
//                // k번째 참가자 => k가 최대 참가자 수보다 작을 경우 다시 반복
//                if(k<p_num) {
//                    k++
//                    main()
////                // 아닐 경우 반복이 되지 않음
//                }
//                else {
//                    btn.text = "결과"
//                    btn.setOnClickListener{
//                        end()
//                    }
//                }
//            }
        }
    }

    // activity_end.xml 실행 함수
    @SuppressLint("SetTextI18n")
    fun end() {
        setContentView(R.layout.activity_end)
        val tv_last: TextView = findViewById(R.id.tv_last)
        val tv_lpoint: TextView = findViewById(R.id.tv_lpoint)
        val btn_init: TextView = findViewById(R.id.btn_init)

        // 꼴찌의 점수 출력 -> maxOrNull() : 점수 리스트에서 가장 큰 수 출력
        tv_lpoint.text = (point_list.maxOrNull()).toString()
        // 점수 리스트에 가장 큰 수를 가진 순위의 참가자 출력
        var index_last = point_list.indexOf(point_list.maxOrNull())
        tv_last.text = "참가자 " + (index_last+1).toString()

        btn_init.setOnClickListener{
            point_list.clear()
            k = 1
            start()
        }
    }

    // 위의 main() 함수 실행
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        start()
//      main()
    }

}

//// 버튼을 누르면 시간이 가고 다시 누르면 멈추는 동작을 할 수 있는 앱 -> 초 단위의 int sec가 1씩 증가
//class MainActivity : AppCompatActivity() {
//
//    }
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//
//        // activity_main.xml를 출력시키는 메소드
//        setContentView(R.layout.activity_main)
//
//        var timerTask : Timer? = null
//
//        var isRunning = false
//        var sec : Int = 0
//        val tv : TextView = findViewById(R.id.tv_random)
//        val tv_t : TextView = findViewById(R.id.tv_timer)
//        val tv_p : TextView = findViewById(R.id.tv_point)
//        val btn : Button = findViewById(R.id.btn_main)
//
//        // 버튼을 클릭하면 1~10 사이에 숫자 중 랜덤한 숫자 출력
//        val random_box = Random()
//        val num = random_box.nextInt(1001)
//        tv.text = (num.toFloat()/100).toString()
//
//
//
//        // setOnClickListener -> 버튼 클릭을 하면 돌아오는 반응을 만듦
//        // 랜덤한 숫자에 최대한 가까이 점수를 맞춘다.
//        btn.setOnClickListener{
//
//            // false -> true => false일 때는 if 문이 돌지 않고 true일 때는 돌아간다.
//            isRunning = !isRunning
//            if(isRunning == true) {
//                                                    // 1000 - 1초 / 100 / 0.1초 ..
//                timerTask = kotlin.concurrent.timer(period = 10) {
//                    sec++
//                    runOnUiThread {
//                                    // 100분의 1로 나눈 int sec는 소수점이 나올 수 없으므로
//                                    // double이나 float 타입으로 캐스팅해서 출력
//                        tv_t.text = (sec.toDouble()/100).toString()
//                    }
//                }
//            } else {
//                timerTask?.cancel()
//                // 랜덤값과 버튼을 눌러서 나온 값의 차이를 출력
//                val point = abs(sec-num).toFloat()/100
//                tv_p.text = point.toString()
//            }
//        }
//    }
//}