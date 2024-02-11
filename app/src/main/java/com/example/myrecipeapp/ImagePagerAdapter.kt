package com.example.myrecipeapp
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter

class ImagePagerAdapter(private val context: Context, private val imageList: List<Int>) : PagerAdapter(){
    override fun getCount(): Int {
        return imageList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return  view == `object` as View
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView: View = inflater.inflate(R.layout.pager_item, container, false)

        val imageViews : Array<ImageView> = arrayOf(
            itemView.findViewById(R.id.imageView1),
            itemView.findViewById(R.id.imageView2),
            itemView.findViewById(R.id.imageView3),
            itemView.findViewById(R.id.imageView4),
            itemView.findViewById(R.id.imageView5),
            itemView.findViewById(R.id.imageView6)
        )

        for( i in 0 until imageViews.size){
            val currentIndex = position + 1
            if (currentIndex < imageList.size){
                imageViews[i].setImageResource(imageList[currentIndex])
            }else{
             imageViews[i].setImageResource(R.drawable.desayunos)
            }
        }

        container.addView(itemView)

        return  itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}