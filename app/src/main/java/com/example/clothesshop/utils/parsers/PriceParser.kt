package com.example.clothesshop.utils.parsers

class PriceParser {

    companion object{

        private fun strPriceToDouble(priceStr: String):Double{
            val strPriceNumber = priceStr.split(" ")[0]
            return strPriceNumber.replace(',','.').toDouble()
        }

        fun sumPrise(list: MutableList<String>):String
        {
            var sum =0.0
            for (l in list){
                sum += strPriceToDouble(l)
            }
            return "$sum ZŁ".replace('.',',')
        }
    }

}