package com.example.mc_project

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment

class ChickenFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chicken, container, false)

        val imgChicken: ImageView = view.findViewById(R.id.imgChicken)
        imgChicken.setOnClickListener {
            val url = "https://www.rankingdak.com/?utm_source=naver_brand&utm_medium=SA&utm_campaign=performance&utm_content=logo&utm_term=%EB%A1%9C%EA%B3%A0&n_media=27758&n_query=%EB%9E%AD%ED%82%B9%EB%8B%AD%EC%BB%B4&n_rank=1&n_ad_group=grp-a001-04-000000042294844&n_ad=nad-a001-04-000000301065183&n_keyword_id=nkw-a001-04-000006221359085&n_keyword=%EB%9E%AD%ED%82%B9%EB%8B%AD%EC%BB%B4&n_campaign_type=4&n_contract=tct-a001-04-000000000901790&n_ad_group_type=5&NaPm=ct%3Dlwp3wweg%7Cci%3D0zC0002PCzfA8HlUuv3w%7Ctr%3Dbrnd%7Chk%3D477b4f6292cf69e730b767c39fedcfb6fb0b1b32"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
        val imgChicken2: ImageView = view.findViewById(R.id.imgChicken2)
        imgChicken2.setOnClickListener {
            val url = "https://www.imdak.com/?&utm_source=naver_bs_pc&utm_medium=brand&utm_campaign=home_link&utm_term=%ED%99%88%EB%A7%81%ED%81%AC&utm_content=%ED%99%88%EB%A7%81%ED%81%AC&n_media=27758&n_query=%EC%95%84%EC%9E%84%EB%8B%AD&n_rank=1&n_ad_group=grp-a001-04-000000041218812&n_ad=nad-a001-04-000000302676429&n_keyword_id=nkw-a001-04-000006108082351&n_keyword=%EC%95%84%EC%9E%84%EB%8B%AD&n_campaign_type=4&n_contract=tct-a001-04-000000000902310&n_ad_group_type=5&NaPm=ct%3Dlwp4fqn4%7Cci%3D0yG0000yDzfA%5F2zdCvi4%7Ctr%3Dbrnd%7Chk%3Dae87f7213fef616185684f4e3fb8b1556eefbe24"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
        val imgChicken3: ImageView = view.findViewById(R.id.imgChicken3)
        imgChicken3.setOnClickListener {
            val url = "https://www.coupang.com/np/search?component=&q=%EB%8B%AD%EA%B0%80%EC%8A%B4%EC%82%B4&channel=user"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }

        return view
    }
}
