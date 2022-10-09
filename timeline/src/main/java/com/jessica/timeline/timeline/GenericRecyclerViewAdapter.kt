package com.jessica.timeline.timeline

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

class GenericRecyclerViewAdapter<T : Any>(
    private val list: ArrayList<T>,
    @LayoutRes val defaultLayoutID: Int,
    private val bindingInterface: RecyclerViewDataBinding<T>
) : RecyclerView.Adapter<GenericRecyclerViewAdapter<T>.TimelineViewHolder>() {

    private var cellTypes = arrayListOf<LayoutType>()

    inner class TimelineViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: T) = bindingInterface.bindData(item, view)
    }

    class LayoutType(val type: HashMap<Int, Int>)

    interface RecyclerViewDataBinding<T> {
        fun bindData(item: Any, view: View)
        fun <T: Any> registerCellType(item: T, position:Int): LayoutType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineViewHolder {
        val layoutID = cellTypes.find { it.type.containsKey(viewType) }
            ?.type?.getValue(viewType)
            ?: run { defaultLayoutID }

        val view = LayoutInflater.from(parent.context).inflate(layoutID, parent, false)
        return (TimelineViewHolder(view))
    }

    override fun onBindViewHolder(holder: TimelineViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemViewType(position: Int): Int {
        val layoutType = bindingInterface.registerCellType(list[position], position)
        cellTypes.add(layoutType)
        return layoutType.type.keys.first()
    }

    override fun getItemCount() = list.size

}