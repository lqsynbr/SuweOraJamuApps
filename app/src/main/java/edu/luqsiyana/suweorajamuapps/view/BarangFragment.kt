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
import edu.luqsiyana.suweorajamuapps.adapter.BarangAdapter
import edu.luqsiyana.suweorajamuapps.databinding.FragmentBarangBinding
import edu.luqsiyana.suweorajamuapps.viewmodel.BarangViewModel

@AndroidEntryPoint
class BarangFragment : Fragment() {

    private var _binding: FragmentBarangBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: BarangViewModel

    private lateinit var adapter: BarangAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBarangBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[BarangViewModel::class.java]

        adapter = BarangAdapter { barang ->
            val bundle = Bundle().apply {
                putParcelable("barang", barang)
            }
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, BarangFormFragment::class.java, bundle)
                .addToBackStack(null)
                .commit()
        }

        binding.recyclerViewBarang.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewBarang.adapter = adapter

        viewModel.barangList.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)

            if (list.isNullOrEmpty()) {
                binding.txtEmptyState.visibility = View.VISIBLE
                binding.recyclerViewBarang.visibility = View.GONE
            } else {
                binding.txtEmptyState.visibility = View.GONE
                binding.recyclerViewBarang.visibility = View.VISIBLE

            }
        }

        binding.btnTambahBarang.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, BarangFormFragment::class.java, null)
                .addToBackStack(null)
                .commit()
        }

        viewModel.loadBarang()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}