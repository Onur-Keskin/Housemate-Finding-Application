package com.onurkeskin.demodemobitirmeproje.model

data class HouseOwnerModel(
    val ownerId:Int,
    val ownerName: String,
    val ownerUsername: String,
    val ownerSurname: String,
    val ownerAge:Int,
    val ownerPassword:String,
    val ownerHometown: String,
    val ownerGrade:String,
    val ownerDepartment: String,
    val ownerPhone: String,
    val ownerMail:String,
    val ownerGender:String
)
/*
{
        "ownerId": 1,
        "ownerName": "Sıla",
        "ownerSurname": "Yılmaz",
        "ownerAge": 22,
        "ownerHometown": "İzmir",
        "ownerGrade": "4",
        "ownerDepatment": "Medical School",
        "ownerPhone": "+90 546413421241241",
        "ownerMail": "siladeneme@gmail.com",
        "ownerGender": "Female"
    }
 */