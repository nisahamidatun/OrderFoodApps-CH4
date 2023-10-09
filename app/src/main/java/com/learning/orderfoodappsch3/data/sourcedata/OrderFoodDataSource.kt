package com.learning.orderfoodappsch3.data.sourcedata

import com.learning.orderfoodappsch3.model.OrderFood

interface OrderFoodDataSource {
    fun getOrderFoods(): List<OrderFood>
}

class OrderFoodDataSourceImpl() : OrderFoodDataSource {
    override fun getOrderFoods(): List<OrderFood> = listOf(
        OrderFood(
            id = 1,
            imgFood = "https://clipart-library.com/image_gallery2/Pizza-PNG-Picture-1.png",
            foodName = "Pizza Paperoni",
            desc = "Frankfurter Sapi, Daging Sapi Cincang, dan Keju Mozzarella",
            foodPrice = 156000.0,
        ),
        OrderFood(
            id = 2,
            imgFood = "https://pngimg.com/d/burger_sandwich_PNG4133.png",
            foodName = "Cheese Burger",
            desc = "Perpaduan dengan 2 Lapisan daging gurih dan 2 lapisan keju disajikan dengan saus tomat",
            foodPrice = 56000.0,
        ),
        OrderFood(
            id = 3,
            imgFood = "https://pngimg.com/d/spaghetti_PNG4.png",
            foodName = "Spageti Bologneese",
            desc = "Pasta kenyal yang dipadukan dengan daging sapi cincang halus dan taburan keju parmesan",
            foodPrice = 80000.0,
        ),
        OrderFood(
            id = 4,
            imgFood = "https://png.pngtree.com/png-clipart/20220116/original/pngtree-design-elements-gourmet-sushi-japanese-cuisine-png-image_7107822.png",
            foodName = "Spam Musubi",
            desc = "nasi putih yang dikepal hingga padat, lalu diberi topping potongan daging Spam, dan dilapisi rumput laut nori",
            foodPrice = 100000.0,
        ),
        OrderFood(
            id = 5,
            imgFood = "https://marugame.co.uk/wp-content/uploads/2021/05/Bowl-Above-NoShaddow-5.png",
            foodName = "Udon",
            desc = "Mie udon disajikan dengan sup kake dashi dan daging sapi sukiyaki impor yang manis juga gurih.",
            foodPrice = 200000.0,
        ),
        OrderFood(
            id = 6,
            imgFood = "https://www.pngkey.com/png/full/912-9129382_-age-takoyaki-7-takoyaki.png",
            foodName = "Takoyaki",
            desc = "Makanan khas jepang berbahan dasar terigu + kaldu ikan berbentuk bulat dengan isian toping di dalamnya dan taburan katsubushi atasnya",
            foodPrice = 400000.0,
        ),
    )
}