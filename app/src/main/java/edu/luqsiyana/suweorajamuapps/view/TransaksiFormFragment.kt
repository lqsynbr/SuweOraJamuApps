package edu.luqsiyana.suweorajamuapps.view

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import edu.luqsiyana.suweorajamuapps.data.model.Transaksi
import edu.luqsiyana.suweorajamuapps.data.model.TransaksiDisplay
import edu.luqsiyana.suweorajamuapps.databinding.FragmentTransaksiFormBinding
import edu.luqsiyana.suweorajamuapps.util.DateTimePickerUtil
import edu.luqsiyana.suweorajamuapps.viewmodel.TransaksiViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch



@AndroidEntryPoint
class TransaksiFormFragment : Fragment() {
    private var _binding: FragmentTransaksiFormBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TransaksiViewModel by viewModels()

    private var transaksiDisplay: TransaksiDisplay? = null

    private var selectedBarangId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTransaksiFormBinding.inflate(inflater, container, false)
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transaksiDisplay = arguments?.getParcelable("transaksi")

        // spinner
        viewModel.barangList.observe(viewLifecycleOwner) { barangList ->
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                barangList.map { it.nama }
            ).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
            binding.spinnerBarang.adapter = adapter


            transaksiDisplay?.let { transaksiItem ->
                val index = barangList.indexOfFirst {
                    it.id == transaksiItem.barangId
                }

                if (index >= 0)
                    binding.spinnerBarang.setSelection(index)
                binding.edtJumlah.setText(transaksiItem.jumlah.toString())
                binding.edtTanggal.setText(transaksiItem.tanggal)
                binding.btnSimpanTransaksi.visibility = View.VISIBLE
                binding.btnHapusTransaksi.visibility = View.VISIBLE
            }

            // dateTimePicker
            binding.edtTanggal.setOnClickListener {
                DateTimePickerUtil.showDateTimePicker(requireContext()) { dateTime ->
                    binding.edtTanggal.setText(dateTime)
                }
            }

            // simpan
            binding.btnSimpanTransaksi.setOnClickListener {
                val jumlahStr = binding.edtJumlah.text.toString()
                val tanggal = binding.edtTanggal.text.toString()
                val jumlah = jumlahStr.toIntOrNull()
                selectedBarangId =
                    barangList.getOrNull(binding.spinnerBarang.selectedItemPosition)?.id

                if (selectedBarangId == null || selectedBarangId == 0) {
                    Toast.makeText(
                        requireContext(),
                        "Pilih barang terlebih dahulu",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    return@setOnClickListener

                }

                if (jumlahStr.isBlank() || jumlah == null || jumlah <= 0) {
                    Toast.makeText(requireContext(), "Jumlah tidak valid", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }

                if (tanggal.isBlank()) {
                    Toast.makeText(
                        requireContext(),
                        "Tanggal Tidak Boleh Kosong",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    return@setOnClickListener
                }

                val newTransaksi = Transaksi(
                    id = transaksiDisplay?.id ?: 0,
                    barangId = selectedBarangId ?: 0,
                    jumlah = jumlah,
                    tanggal = tanggal
                )

                binding.progressBar.visibility = View.VISIBLE
                binding.btnSimpanTransaksi.isEnabled = false

                lifecycleScope.launch {
                    if (transaksiDisplay == null) {
                        viewModel.addTransaksi(newTransaksi)
                        delay(300)
                        viewModel.loadTransaksi()
                        Toast.makeText(
                            requireContext(),
                            "Transaksi berhsail ditambahkan",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        viewModel.updateTransaksi(newTransaksi)
                        delay(300)
                        viewModel.loadTransaksi()
                        Toast.makeText(
                            requireContext(),
                            "Transaksi berhsail diperbaharui",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    binding.progressBar.visibility = View.GONE
                    binding.btnSimpanTransaksi.isEnabled = true
                    parentFragmentManager.popBackStack()
                }
            }

            binding.btnHapusTransaksi.setOnClickListener {
                transaksiDisplay?.let { transaksiToDelete ->
                    AlertDialog.Builder(requireContext())
                        .setTitle("Konfirmasi Hapus Transaksi")
                        .setMessage("Yakin ingin menghapus Transaksi ini?")
                        .setPositiveButton("Hapus") { _, _ ->
                            lifecycleScope.launch {
                                viewModel.deleteTransaksi(transaksiToDelete.toTransaksi())
                                Toast.makeText(
                                    requireContext(),
                                    "Transaksi Berhasil di Hapus",
                                    Toast.LENGTH_SHORT
                                ).show()
                                delay(300 )
                                viewModel.loadTransaksi()
                                parentFragmentManager.popBackStack()
                            }
                        }
                        .setNegativeButton("Batal", null)
                        .show()
                }
            }
        }
        viewModel.loadBarang()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}