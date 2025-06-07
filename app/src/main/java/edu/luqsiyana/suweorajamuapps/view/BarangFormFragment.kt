package edu.luqsiyana.suweorajamuapps.view

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import edu.luqsiyana.suweorajamuapps.R
import edu.luqsiyana.suweorajamuapps.data.model.Barang
import edu.luqsiyana.suweorajamuapps.databinding.FragmentBarangFormBinding
import edu.luqsiyana.suweorajamuapps.util.ImageUtils.copyImageToInternalStorage
import edu.luqsiyana.suweorajamuapps.viewmodel.BarangViewModel
import kotlinx.coroutines.launch
import java.io.File
import kotlinx.coroutines.delay

@AndroidEntryPoint
class BarangFormFragment : Fragment() {

    private var _binding: FragmentBarangFormBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BarangViewModel by viewModels()

    private var barang: Barang? = null
    private var imageUri: Uri? = null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        it?.let { uri ->
            val safeUri = copyImageToInternalStorage(requireContext(), uri)
            safeUri?.let { newUri ->
                imageUri = newUri
                binding.imgPreview.setImageURI(newUri)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBarangFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        barang = arguments?.getParcelable("barang")

        barang?.let {
            binding.edtNama.setText(it.nama)
            binding.edtHarga.setText(it.harga.toString())
            imageUri = Uri.parse(it.imageUri)

            val file = File(imageUri?.path ?: "")
            if (file.exists()) {
                binding.imgPreview.setImageURI(imageUri)
            } else {
                binding.imgPreview.setImageResource(R.drawable.placeholder)
            }
            binding.btnHapusBarang.visibility = View.VISIBLE
        }

        binding.imgPreview.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        binding.btnPilihDariGaleri.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        binding.btnSimpanBarang.setOnClickListener {
            val nama = binding.edtNama.text.toString()
            val harga = binding.edtHarga.text.toString().toDoubleOrNull()

            if (nama.isBlank() || harga == null || imageUri == null) {
                Toast.makeText(requireContext(), "Isi semua field dan pilih gambar", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.progressBar.visibility = View.VISIBLE
            binding.btnSimpanBarang.isEnabled = false

            lifecycleScope.launch {
                val barangBaru = Barang (
                    id = barang?.id ?: 0,
                    nama = nama,
                    harga = harga,
                    imageUri = imageUri.toString()
                )

                if (barang == null) {
                    viewModel.insertBarang(barangBaru)
                    Toast.makeText(requireContext(), "Barang berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.updateBarang(barangBaru)
                    Toast.makeText(requireContext(), "Barang berhasil diupdate", Toast.LENGTH_SHORT).show()
                }

                delay(500)
                binding.progressBar.visibility = View.GONE
                binding.btnSimpanBarang.isEnabled = true
                parentFragmentManager.popBackStack()
            }
        }

        binding.btnHapusBarang.setOnClickListener {
            barang?. let {
                AlertDialog.Builder(requireContext())
                    .setTitle("Konfirmasi")
                    .setMessage("Apakah Anda yakin ingin menghapus barang ini?")
                    .setPositiveButton("Hapus") { _, _ ->
                        lifecycleScope.launch {
                            viewModel.deleteBarang(it)
                            Toast.makeText(
                                requireContext(),
                                "Barang berhasil dihapus",
                                Toast.LENGTH_SHORT
                            ).show()
                            parentFragmentManager.popBackStack()
                        }
                    }
                    .setNegativeButton("Batal", null)
                    .show()

            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}