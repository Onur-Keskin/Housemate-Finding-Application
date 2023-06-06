package com.onurkeskin.demodemobitirmeproje.model

import com.google.gson.JsonObject

data class HouseModel(
    val houseId:Int,
    val houseAddress:String,
    val countOfBathroom:Int,
    val countOfBedroom:Int,
    val countOfSalon:Int,
    val countOfOwner:Int,
    val houseType:String,
    val furnished : Boolean,
    val heatResource:String,
    val internetPaved:String,
    val floor:Int,
    val rent:Int,
    val owners:List<JsonObject>,//Burası json gibi bir obje olabilir veya sahip adını tutarız buraya tıklayınca houseOwner'ın profiline gider
    //val customerId:List<JsonObject>//Burası json gibi bir obje olabilir veya sahip adını tutarız buraya tıklayınca customer'ın profiline gider

)

/*
Gelen veri bu şekilde
    {
        "houseId": 1,
        "houseAddress": "Konaklar",
        "countOfBathroom": 1,
        "countOfBedroom": 1,
        "countOfSalon": 1,
        "countOfOwner": 1,
        "heatResource": "gas",
        "furnished": "yes",
        "internetPaved": "Yes",
        "floor": 3,
        "rent": 3500,
        "ownerId": [
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
        ],
        "customerId": [
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
        ]
    }
 */