package edu.luqsiyana.suweorajamuapps.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.luqsiyana.suweorajamuapps.data.model.Transaksi
import edu.luqsiyana.suweorajamuapps.data.model.TransaksiBarang
import edu.luqsiyana.suweorajamuapps.databinding.ItemTransaksiBinding

class TransaksiAdapter(
    private val onClick: (TransaksiBarang) -> Unit
): ListAdapter<TransaksiBarang, TransaksiAdapter.ViewHolder> (DiffCallback()) {

    class ViewHolder(private val binding: ItemTransaksiBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(transaksi: TransaksiBarang, onCLick: (TransaksiBarang) -> Unit) {

            val totalBayar = transaksi.barang.harga * transaksi.transaksi.jumlah

            binding.txtNamaBarang.text = transaksi.barang.nama
            binding.txtHargaBarang.text = "Harga: ${transaksi.barang.harga}"
            binding.txtJumlah.text = "Jumlah: ${transaksi.transaksi.jumlah}"
            binding.txtTanggal.text = "Tanggal: ${transaksi.transaksi.tanggal}"

            val formattedTotal =
                java.text.NumberFormat.getCurrencyInstance(java.util.Locale("in", "ID"))
                    .format(totalBayar)
            binding.txtTotalBiaya.text = "Total: $formattedTotal"

            Glide.with(binding.root.context)
                .load(transaksi.barang.imageUri)
                .into(binding.imgBarang)

            binding.root.setOnClickListener {
                onCLick(transaksi)
            }
        }
    }

    fun getTotalBiaya(): Double {
        return  currentList.sumOf { it.barang.harga * it.transaksi.jumlah }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTransaksiBinding.inflate(
            LayoutInflater.from(parent.context),parent, false
        )
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaksi = getItem(position)
        holder.bind(transaksi, onClick)
    }

    class DiffCallback : DiffUtil.ItemCallback<TransaksiBarang>() {
        override fun areItemsTheSame(
            oldItem: TransaksiBarang,
            newItem: TransaksiBarang
        ): Boolean {
            return oldItem.transaksi.id == newItem.transaksi.id
        }

        override fun areContentsTheSame(
            oldItem: TransaksiBarang,
            newItem: TransaksiBarang
        ) : Boolean {
            return oldItem == newItem
        }
    }
}