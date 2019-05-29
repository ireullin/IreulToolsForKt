package libs.tokenizers

import libs.tokenizers.cutters.ChineseCutter
import libs.tokenizers.cutters.EnglishCutter
import libs.tokenizers.cutters.NgramCutter

/**
 * 常用的TokenBuilder設定
 */
fun usualTokenizer1(target:List<String>, callback:((TokenBuilder.Status)->Unit)?=null): TokenBuilder {
    return TokenBuilder(target).dumpStatus("origin",callback)
            .cutBy(ChineseCutter(NgramCutter(2))).dumpStatus("cut with chinese bigram",callback)
            .cutBy(EnglishCutter()).dumpStatus("cut with english charters",callback)
            .dropIf{ i, t -> t.trim().length < 2 }.dumpStatus("drop length less than 2",callback)
            // 不是全部中文 ＆不是全英文 ＆ 不是全數字 ＝ 符號
            .dropIf({ i, t -> !areAllChineseChars(t) && !areAllEnglishChars(t) && !isNumber(t) }).dumpStatus("drop symbols",callback)
            // 全部英文而且長度小於等於2 (ml cc etc.)
            .dropIf{ i, t -> areAllEnglishChars(t) && t.length <= 2 }.dumpStatus("drop english term which length less than 2",callback)
            .map{ i, token -> token.toLowerCase() }.dumpStatus("to lower case",callback)
            .dropRedundancy().dumpStatus("drop redundancy",callback)
}


/**
 * java8 提供的是否為漢文的判斷方式
 */
fun isHanSentence(s:String):Boolean {
    return s.codePoints().anyMatch {codepoint-> Character.UnicodeScript.of(codepoint) == Character.UnicodeScript.HAN}
}

/**
 * 是否全部為中文字
 */
fun areAllChineseChars(s: String): Boolean {
    try {
        for (c in s.toCharArray()) {
            if (!isChineseChar(c))
                return false
        }
        return true
    } catch (e: Exception) {
        return false
    }
}

/**
 * 根据Unicode编码完美的判断中文汉字和符号
 */
fun isChineseChar(c: Char): Boolean {
    val ub = Character.UnicodeBlock.of(c)
    return if (ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
            || ub === Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
            || ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
            || ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
            || ub === Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
            || ub === Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
            || ub === Character.UnicodeBlock.GENERAL_PUNCTUATION) {
        true
    } else false
}

fun isEnglishChar(c: Char): Boolean {
    return when{
        (c >= 'a' && c <= 'z') -> true
        (c >= 'A' && c <= 'Z') -> true
        else -> false
    }
}

fun areAllEnglishChars(s: String, ignoreChars:Set<Char> = setOf()): Boolean {
    try {
        for (c in s.toCharArray()) {
            if (c in ignoreChars) continue
            if (isEnglishChar(c)) continue
            return false
        }
        return true
    } catch (e: Exception) {
        return false
    }
}

fun isNumber(s: String): Boolean {
    try {
        Integer.parseInt(s)
        return true
    } catch (e: Exception) {
        return false
    }
}
