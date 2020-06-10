package com.kakao.smartmemo.Contract

import com.kakao.smartmemo.Data.PlaceData
import com.kakao.smartmemo.Data.TodoData

interface AddTodoContract {
    interface View {
        fun onSuccess(placeList: MutableList<PlaceData>)
        fun onAddSuccess()
    }
    interface Presenter {
        fun addTodo(todoData: TodoData, placeList: ArrayList<PlaceData>)
        fun setTodoDateAdapterModel(adapterModel : TodoDateAdapterContract.Model)
        fun setTodoDateAdapterView(adapterView : TodoDateAdapterContract.View)
        fun setTodoPlaceAdapterModel(adapterModel : TodoPlaceAdapterContract.Model)
        fun setTodoPlaceAdapterView(adapterView : TodoPlaceAdapterContract.View)
        fun getList(): MutableList<PlaceData>
        fun getPlace(status: String)
    }
    interface OnAddTodoListener {
        fun onSuccess(placeList: MutableList<PlaceData>)
        fun onFailure()
        fun onAddSuccess()
    }
}