package libs.tokenizers.cutters

import org.junit.Test

class GarbageCutterTest{
    @Test
    fun test1() {
        val s = "倡导SIDE国家主义和新人类学说的激进思想家吉翁·什姆·戴肯批评联邦政府的政策，鼓吹宇宙殖民地独立运动。在U.C.0050年，各地开始要求自治的示威活动，却遭驻留联邦军的镇压，使得宇宙住民对联邦的不满进一步升级。U.C.0053年，前往离地球最远的SIDE 3的吉翁·戴肯当选SIDE 3首相，并于U.C.0058年发表独立宣言，吉翁共和国成立，戴肯被尊为国父。此举引发各殖民地独立运动的激化，联邦政府开始对SIDE 3实施经济制裁，并开始扩充军备。而SIDE 3则加强与其它殖民地的往来，以对抗联邦的经济制裁。"
        GarbageCutter().cut(s).foreach{i,k->println(k)}
        println("=============================")
        GarbageCutter().addSymbols('·').cut(s).foreach{i,k->println(k)}
        println("=============================")
        GarbageCutter(setOf('。')).cut(s).foreach{i,k->println(k)}
    }
}