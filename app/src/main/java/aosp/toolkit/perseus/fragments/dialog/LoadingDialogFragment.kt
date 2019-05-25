package aosp.toolkit.perseus.fragments.dialog

import android.os.Bundle
import android.support.v4.app.DialogFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import aosp.toolkit.perseus.R


/*
 * @File:   LoadingDialogFragment
 * @Author: 1552980358
 * @Time:   6:11 PM
 * @Date:   20 Apr 2019
 * 
 */

class LoadingDialogFragment: DialogFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialogfragment_loading, container, false)
    }
}