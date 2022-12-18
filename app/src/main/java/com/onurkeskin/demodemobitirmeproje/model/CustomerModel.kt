package com.onurkeskin.demodemobitirmeproje.model

data class CustomerModel(
    val customerId:Int,
    val customerName:String,
    val customerUserName:String,
    val customerSurname:String,
    val customerAge:Int,
    val customerHometown:String,
    val customerDepartment:String,
    val customerGrade:Int,
    val customerPhone:String,
    val customerEmail:String,
    val customerGender:String
)

/*
  {
        "customerId": 1,
        "customerName": "Hüseyin",
        "customerSurname": " Türkmen",
        "customerAge": 22,
        "customerHometown": "Konya",
        "customerDepartment": "Computer Eng.",
        "customerGrade": 4,
        "customerPhone": "+31 41209410",
        "customerEmail": "hnr@gmail.com",
        "customerGender": "Male"
    }
 */