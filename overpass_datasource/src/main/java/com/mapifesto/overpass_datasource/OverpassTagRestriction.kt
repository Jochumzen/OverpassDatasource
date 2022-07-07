package com.mapifesto.overpass_datasource

//A single restriction such as "amenity = cafe"
data class OverpassTagRestriction(
    val key: String,
    val value: String?,
    val exact: Boolean = true,          //if exact = true only items where value is *exactly* what we specified is returned.
                                        //If exact = false and, for example. key = name, value = coffee, all items with coffee somewhere in value is returned (eg Waynes coffee)
    val ignoreCase: Boolean = false,    //if false, case must be exact for a match
) {
    fun asOverpassString(): String {
        val str: StringBuilder = java.lang.StringBuilder()
        str.append("[$key")
        if(value != null) {
            if(exact)
                str.append("=")
            else
                str.append("~")

            str.append(value)

            if(ignoreCase)
                str.append(",i")
        }
        str.append("]")
        return str.toString()
    }
}

//A collection of restrictions that should be interpreted as OR.
//e.g. "cuisine=chinese", "cuisine=thai"
//key is probably the same for all items
data class UnionOfOverpassTagRestrictions(
    val restrictions: List<OverpassTagRestriction>
)

//A collection unions that should be interpreted as OR.
//e.g. item 1: amenity=restaurant. item 2: "cuisine=chinese", "cuisine=thai"
//restaurant AND (chinese OR thai)
data class IntersectionOfOverpassTagRestrictions(
    val restrictionUnions: List<UnionOfOverpassTagRestrictions>
) {


    private fun item(array: IntArray): String {
        val str: StringBuilder = java.lang.StringBuilder()
        restrictionUnions.forEachIndexed { index, union ->
            str.append(union.restrictions[array[index]].asOverpassString() )
        }
        return str.toString()
    }

    fun overpassString(bbox: String): String {

        val unionSize = restrictionUnions.size      // Example 4
        var arraysize = IntArray(unionSize)         // 0,0,0, 0 ->

        var N = 1

        for (i in 0 until unionSize) {
            val itemSize = restrictionUnions[i].restrictions.size
            N *= itemSize
            arraysize[i] = itemSize
        }                                           //Example: 3,1,2,3  (r11 || r12 || r13) && r2 && (r31 || r32) && (r41 || r42 || r43)

        var n = 1
        var r = 1

        var columns = Array(size = unionSize, init = {IntArray(N)} )

        for (i in unionSize - 1 downTo 0) {
            r *= n
            n = arraysize[i]
            val c = N.div(r*n)
            columns[i] = createColumn(n,r,c)
        }

        var rows = Array(size = N, init = {IntArray(unionSize)} )

        for(i in 0 until N) {

            var row = IntArray(unionSize)
            for(j in 0 until unionSize) {
                row[j] = columns[j][i]
            }
            rows[i] = row
        }

        val sb = StringBuilder()

        sb.append("[out:json][timeout:20];(")
        rows.forEach {
            sb.append("nwr")
            sb.append(item(it))
            sb.append(bbox)
            sb.append(";")
        }
        sb.append(");out center;")
        return sb.toString()
    }


    //Ex n = 4 r = 3, c = 2 (N = 24):
    // 0 0 0 1 1 1 2 2 2 3 3 3 0 0 0 1 1 1 2 2 2 3 3 3
    // n = 4 makes it go 0,1,2,3. r = 3 makes it repeat each number 3 times. c = 2 does the whole n-r thing (0 0 0 1 1 1 2 2 2 3 3 3) twice.
    private fun createColumn(n:Int, r: Int, c:Int): IntArray {

        var pos = 0
        val arraySize = n*r*c
        val x = IntArray(arraySize)

        for(ci in 0 until c) {
            for(ni in 0 until n) {
                for(ri in 0 until r) {
                    x[pos] = ni
                    pos += 1
                }
            }
        }
        return x
    }
}
