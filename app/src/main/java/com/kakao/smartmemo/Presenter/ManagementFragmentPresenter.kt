package com.kakao.smartmemo.Presenter

import com.kakao.smartmemo.Contract.ManagementAdapterContract
import com.kakao.smartmemo.Contract.ManagementFragmentContract
import com.kakao.smartmemo.Model.FolderModel

class ManagementFragmentPresenter : ManagementFragmentContract.Presenter{
    private var view : ManagementFragmentContract.View
    private var model :FolderModel
    private lateinit var adapterView : ManagementAdapterContract.View
    private lateinit var adapterModel: ManagementAdapterContract.Model

    constructor(view : ManagementFragmentContract.View){
        this.view = view
        this.model = FolderModel()
    }

    override fun setManagementAdapterView(adapter: ManagementAdapterContract.View) {
        this.adapterView = adapter
    }

    override fun setManagementAdapterModel(adapter: ManagementAdapterContract.Model) {
        this.adapterModel = adapter
    }
}