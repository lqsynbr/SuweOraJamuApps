package edu.luqsiyana.suweorajamuapps.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import edu.luqsiyana.suweorajamuapps.R
import edu.luqsiyana.suweorajamuapps.adapter.TransaksiAdapter
import edu.luqsiyana.suweorajamuapps.data.model.TransaksiDisplay
import edu.luqsiyana.suweorajamuapps.databinding.FragmentTransaksiBinding
import edu.luqsiyana.suweorajamuapps.viewmodel.TransaksiViewModel
import java.text.NumberFormat
import java.util.Locale


@AndroidEntryPoint
class TransaksiFragment : Fragment() {

    private var _binding: FragmentTransaksiBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: TransaksiViewModel
    private lateinit var adapter: TransaksiAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTransaksiBinding.inflate(inflater, container, false)
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[TransaksiViewModel::class.java]

        adapter = TransaksiAdapter { transaksi ->
            val display = TransaksiDisplay (
                id = transaksi.transaksi.id,
                barangId = transaksi.barang.id,
                jumlah = transaksi.transaksi.jumlah,
                tanggal = transaksi.transaksi.tanggal
            )
            val bundle = Bundle().apply {
                putParcelable("transaksi", display)
            }
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, TransaksiFormFragment::class.java, bundle)
                .addToBackStack(null)
                .commit()

        }

        //conf recyclerView
        binding.recyclerViewTransaksi.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewTransaksi.adapter = adapter

        //viewmodel list transaksi
        viewModel.transaksiList.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list.toList())

            //empty logic
            if (list.isNullOrEmpty()) {
                binding.txtemptyTransaksi.visibility = View.VISIBLE
                binding.recyclerViewTransaksi.visibility = View.GONE
            } else {
                binding.txtemptyTransaksi.visibility = View.GONE
                binding.recyclerViewTransaksi.visibility = View.VISIBLE
            }

            val totalBiaya = adapter.getTotalBiaya()
            val formatted = NumberFormat.getCurrencyInstance(
                Locale("in", "ID")).format(totalBiaya)
            binding.txtTotalBiaya.text = "Total Biaya: $formatted"
        }

        binding.btnTambahTransaksi.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, TransaksiFormFragment())
                .addToBackStack(null)
                .commit()
        }

        viewModel.loadTransaksi()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}