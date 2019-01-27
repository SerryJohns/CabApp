package com.serionz.mytaxi.adapters


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.serionz.mytaxi.R
import com.serionz.mytaxi.data.Cab
import com.serionz.mytaxi.data.FleetType
import kotlinx.android.synthetic.main.layout_cab_item.view.*


class CabsListAdapter(private val cabsList: List<Cab>?) : RecyclerView.Adapter<CabsListAdapter.ViewHolder>() {
    var itemClickListener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_cab_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(cabsList?.get(position))


    override fun getItemCount(): Int = cabsList?.size ?: 0

    inner class ViewHolder(private val mView: View) : RecyclerView.ViewHolder(mView) {
        fun bind(cab: Cab?) {
            if (cab != null) {
                with(mView) {
                    when (cab.fleetType) {
                        FleetType.TAXI -> {
                            cab_fleet_type.text = resources.getString(R.string.taxi)
                            cab_icon.setImageResource(R.drawable.ic_taxi_cab_front)
                        }
                        FleetType.POOLING -> {
                            cab_fleet_type.text = resources.getString(R.string.car_pool)
                            cab_icon.setImageResource(R.drawable.ic_car_pool_front)
                        }
                    }
                    cab_address.text = cab.address
                    setOnClickListener { itemClickListener?.onCabItemClick(cab) }
                }
            }
        }
    }

    interface ItemClickListener {
        fun onCabItemClick(cab: Cab)
    }
}
