package libs.tokenizers

import libs.tokenizers.cutters.BigramCutter
import libs.tokenizers.cutters.ChineseCutter
import libs.tokenizers.cutters.EnglishCutter

object UsualTokenizers{
        fun composit1(target: String, saveStatus:Boolean=false): TokenBuilder {
            return TokenBuilder(target, saveStatus).saveStatus("origin")
                    .cutBy(ChineseCutter(BigramCutter())).saveStatus("cut with chinese bigram")
                    .cutBy(EnglishCutter()).saveStatus("cut with english charters")
                    .dropIfMatch({ i, t -> t.trim().length < 2 }).saveStatus("drop length less than 2")
                    // 不是全部中文 ＆不是全英文 ＆ 不是全數字 ＝ 符號
                    .dropIfMatch({ i, t -> !ChineseCutter.isAllChinese(t) && !EnglishCutter.isAllEnglish(t) && !TokenBuilder.isAllNumber(t) }).saveStatus("drop symbols")
                    // 全部英文而且長度小於等於2 (ml cc etc.)
                    .dropIfMatch({ i, t -> EnglishCutter.isAllEnglish(t) && t.length <= 2 }).saveStatus("drop english term which length less than 2")
                    .map({ i, token -> token.toLowerCase() }).saveStatus("to lower case")
//                    .dropRedundancy().saveStatus("drop redundancy")
        }
}