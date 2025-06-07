package edu.luqsiyana.suweorajamuapps.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.luqsiyana.suweorajamuapps.data.model.Barang
import edu.luqsiyana.suweorajamuapps.databinding.ItemBarangBinding

class BarangAdapter (private val onClick: (Barang) -> Unit
    ) : ListAdapter<Barang, BarangAdapter.ViewHolder>(DiffCallback()) {
    class ViewHolder(private val binding: ItemBarangBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(barang: Barang, onClick: (Barang) -> Unit) {

            binding.txtNamaBarang.text = barang.nama

            val formattedHarga = java.text.NumberFormat
                .getCurrencyInstance(java.util.Locale("in", "ID"))
                .format(barang.harga)
            binding.txtHargaBarang.text = "Harga: Rp$formattedHarga"

            Glide.with(binding.root.context)
                .load(barang.imageUri)
                .into(binding.imgBarang)

            binding.root.setOnClickListener {
                onClick(barang)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Barang>() {

        override fun areItemsTheSame(oldItem: Barang, newItem: Barang): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Barang, newItem: Barang): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBarangBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val barang = getItem(position)
            holder.bind(barang, onClick)
        }
}
