package aosp.toolkit.perseus.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout

import aosp.toolkit.perseus.R
import aosp.toolkit.perseus.base.BaseManager

import kotlinx.android.synthetic.main.view_navigation.view.*

/*
 * OsToolkit - Kotlin
 *
 * Date : 28 Mar 2019
 *
 * By   : 1552980358
 *
 */


class SideNavigationView(ctx: Context, attributeSet: AttributeSet) : LinearLayout(ctx) {
    init {
        LayoutInflater.from(ctx).inflate(R.layout.view_navigation, this)
        val typedArray = ctx.obtainStyledAttributes(attributeSet, R.styleable.SideNavigationView)

        // 设置HeaderLayout布局 Set HeaderLayout
        if (typedArray.hasValue(R.styleable.SideNavigationView_headerLayout)) {
            headerLayout.addView(
                LayoutInflater.from(ctx).inflate(
                    typedArray.getResourceId(
                        R.styleable.SideNavigationView_headerLayout,
                        0
                    ), null
                )
            )
        } else {
            headerLayout.visibility = View.GONE
        }

        // 设置BottomLayout布局 Set BottomLayout
        if (typedArray.hasValue(R.styleable.SideNavigationView_bottomLayout)) {
            bottomLayout.addView(
                LayoutInflater.from(ctx).inflate(
                    typedArray.getResourceId(
                        R.styleable.SideNavigationView_bottomLayout,
                        0
                    ), null
                )
            )
        } else {
            bottomLayout.visibility = View.GONE
        }

        // 设置Menu Set Menu
        if (typedArray.hasValue(R.styleable.SideNavigationView_menu)) {
            navigationView.inflateMenu(typedArray.getResourceId(R.styleable.SideNavigationView_menu, 0))
        }

        //设置itemIconTint Set itemIconTint
        if (typedArray.hasValue(R.styleable.SideNavigationView_itemIconTint)) {
            navigationView.itemIconTintList = typedArray.getColorStateList(R.styleable.SideNavigationView_itemIconTint)
        } else {
            navigationView.itemIconTintList = null
        }

        typedArray.recycle()

        navigationView.setNavigationItemSelectedListener(BaseManager.getInstance().mainActivity)
    }
}