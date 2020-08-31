package libs.algorithms

import libs.datetime.ImmutableDatetime
import org.slf4j.LoggerFactory
import java.math.BigInteger
import java.util.*

object IdGenerator{
    val log = LoggerFactory.getLogger(this.javaClass)

    val limit = 100 // 剩幾筆的時候就要開始產生
    val batchSize = 1000 // 一次產生的筆數,如果會產生碰撞請將此值加大

    private val numMapChar = run{
        val eng = "qweazxcvbjhgfdsmrtyuioplkn"
////        eng.reversed()  +
        "4501236789" + eng.toUpperCase()
    }.toCharArray()

    private val charMapNum = numMapChar.mapIndexed { i, c -> c to i }.toMap()
    private val queue = LinkedList<String>()
    private var isGenerating = false


    fun decrypt(s:String): BigInteger {
        var sum = BigInteger.ZERO
        val charSize = charMapNum.size.toBigInteger()
        s.forEachIndexed {idx,n->
            val i = s.length-idx-1
            val t = charSize.pow(i)
            sum += t.times(charMapNum[n]!!.toBigInteger())
        }
        return sum
    }

    fun encrypt(num: BigInteger):String{
        if(num== BigInteger.ZERO)
            return numMapChar[0].toString()

        val buff = StringBuffer(100)
        val numAllChars = BigInteger.valueOf(numMapChar.size.toLong())
        var dividendn = num
        while (dividendn > BigInteger.ZERO){
            if(dividendn < numAllChars){
                buff.append(numMapChar[dividendn.toInt()]!!)
                dividendn = BigInteger.ZERO // 除完就出去了
            }else{
                val remainder = dividendn % numAllChars
                buff.append(numMapChar[remainder.toInt()]!!)
                dividendn = (dividendn-remainder)/ numAllChars
            }
        }
        return buff.reverse().toString()
    }

    private fun evaluteChecksum(s:String):Char{
        val prime = BigInteger.valueOf(7)
        val sum = s.mapIndexed{i,c -> prime.pow(i).times( BigInteger.valueOf(c.toLong()) ) }
                .fold(BigInteger.ZERO, BigInteger::add)
        val remainder = sum.mod(numMapChar.size.toBigInteger()).toInt()
        return numMapChar[ remainder ]
    }


    private fun generateIds(n:Int){
        isGenerating = true
        try{
            val nPow = n.toString().length+1
            val carry = BigInteger.TEN.pow(nPow)
            val buff = mutableListOf<BigInteger>()
            var cnt = 0
            while (buff.size<n){
                cnt += 1
                // 結尾為零倒過來會少一位,所以避開
                if(cnt% 10==0)
                    continue

                val stamp = ImmutableDatetime.now().stamp().toBigInteger()
                val sum = (stamp * carry )+ cnt.toBigInteger()
                buff.add(sum)
            }

            val ids = buff
                    .map { it.toString().reversed() }
                    .map{
                        val checksum = evaluteChecksum(it)
                        val enc = encrypt(it.toBigInteger())
                        enc+checksum
                    }
                    .shuffled()

            queue.addAll(ids)
            log.info("generated ${ids.size} ids")
        }catch (e:Exception){
            log.warn("generate ids failed", e)
        }finally {
            isGenerating = false
        }
    }

    fun getId():String{
        if(queue.size< limit){
            generateIds(batchSize)
        }

        return queue.poll()!!
    }

    fun peekBuffer():List<String>{
        return queue.toList()
    }

    fun verify(s:String):Boolean{
        try {
            val checksum = s.last()
            val v = s.dropLast(1)
            val dec = decrypt(v)
            return checksum == evaluteChecksum(dec.toString())
        }catch (e:Exception){
            return false
        }
    }
}