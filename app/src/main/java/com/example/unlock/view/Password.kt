package com.example.unlock.view

import `in`.aabhasjindal.otptextview.OTPListener
import android.content.Intent
import android.os.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.unlock.Hello
import com.example.unlock.databinding.ActivityPasswordBinding

class Password : AppCompatActivity() {
    private lateinit var binding: ActivityPasswordBinding
    var passCode: String = ""
    var otpString: String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.firstPinNew.requestFocusOTP()
        binding.firstPinNew.otpListener = object :

            OTPListener{
            override fun onInteractionListener() {

            }

            override fun onOTPComplete(otp: String) {
                if (passCode.isEmpty()){
                    this@Password.passCode = otp
                    binding.firstPinNew.setOTP("")
                }else{
                    if (passCode == otp){
                        Toast.makeText(this@Password,"Thanh cong otp: $otp", Toast.LENGTH_SHORT).show()
                        binding.firstPinNew.setOTP("")
                        startActivity(Intent(this@Password, Hello::class.java))
                    }else{
                        Toast.makeText(this@Password,"Sai otp: $otp", Toast.LENGTH_SHORT).show()
                        binding.firstPinNew.setOTP("")
                    }
                }
                this@Password.otpString =""
            }
        }
        btnNumberClick(binding)
    }
    private fun btnNumberClick(binding: ActivityPasswordBinding){
        binding.btn1.setOnClickListener {
            addText("1")
        }

        binding.btn2.setOnClickListener {
            addText("2")
        }

        binding.btn3.setOnClickListener {
            addText("3")
        }

        binding.btn4.setOnClickListener {
            addText("4")
        }
        binding.btn5.setOnClickListener {
            addText("5")
        }
        binding.btn6.setOnClickListener {
            addText("6")
        }
        binding.btn7.setOnClickListener {
            addText("7")
        }
        binding.btn8.setOnClickListener {
            addText("8")
        }
        binding.btn9.setOnClickListener {
            addText("9")
        }
        binding.btn00.setOnClickListener {
            addText("0")
        }
        binding.btnDelete.setOnClickListener {
            if (otpString.isNotEmpty()){
                otpString = otpString.substring(0, otpString.length-1)
                binding.firstPinNew.setOTP(otpString)
            }else{
                Toast.makeText(this,"Nhap code",Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnClear.setOnClickListener{
            otpString = ""
            binding.firstPinNew.setOTP(otpString)
        }
        binding.btnBack.setOnClickListener {
            startActivity(Intent(this@Password, MainActivity::class.java))
        }

    }

    private fun addText(s: String) {
        otpString += s
        binding.firstPinNew.setOTP(otpString)
    }
}





