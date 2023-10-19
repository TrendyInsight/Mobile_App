package com.example.fashiondetectv11

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.media.browse.MediaBrowser.MediaItem
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.blogspot.atifsoftwares.circularimageview.CircularImageView
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.fashiondetectv11.R
import java.security.PrivilegedAction

class AddUpdateRecordActivity : AppCompatActivity() {
    private val CAMERA_REQUEST_CODE=100
    private val STORAGE_REQUEST_CODE=101

    private val IMAGE_PICK_CAMERA_CODE=102
    private val IMAGE_PICK_GALLERY_CODE=103

    private lateinit var cameraPermission: Array<String>
    private lateinit var storagePermission: Array<String>
    private lateinit var  pNameEt:EditText
    private lateinit var  pRateEt:EditText
    private lateinit var  pReasonEt:EditText
    private  lateinit var  pImageView: CircularImageView
    private lateinit var  saveBtn: FloatingActionButton
    private var imageUri:Uri?=null
    private var name:String?=""
    private var rate:String?=""
    private var reason:String?=""

    private var actionBar:ActionBar?=null;
    lateinit var dbHelper:MyDBHelper

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_update_record)

        actionBar=supportActionBar
        actionBar!!.title="Add Record"

        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar!!.setDisplayShowHomeEnabled(true)

        dbHelper= MyDBHelper(this)

        cameraPermission= arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        storagePermission= arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        pImageView =findViewById(R.id.personImage)
        pImageView.setOnClickListener{
            imagePickDialog()
        }
        saveBtn = findViewById(R.id.saveBtn)
        saveBtn.setOnClickListener{
            inputData()
        }
        pNameEt =findViewById(R.id.nameEt)
        pRateEt =findViewById(R.id.rateEt)
        pReasonEt =findViewById(R.id.reasonEt)
    }
    private fun inputData() {
        name = ""+pNameEt.text.toString().trim()
        rate = ""+pRateEt.text.toString().trim()
        reason = ""+pReasonEt.text.toString().trim()
        val timestamp=System.currentTimeMillis()
        val id=dbHelper.insertRecord(
            "" +name,
            ""+ imageUri,
            ""+rate,
            ""+reason,
            ""+timestamp,
            ""+timestamp
        )
        Toast.makeText(this,"Record Added against ID $id", Toast.LENGTH_SHORT).show()
    }

    private fun imagePickDialog() {
        val options= arrayOf("Camera","Gallery")
        val builder=AlertDialog.Builder(this)
        builder.setTitle("Pick Image From")
        builder.setItems(options){dialog,which->
            if(which==0){
                if (!checkCameraPermission()){
                    requestCameraPermission()
                }else{
                    pickFromCamera()
                }
            }else{
                if(!checkStoragePermission()){
                    requestStoragePermission()
                }else{
                    pickFromGallery()
                }
            }
        }
        builder.create().show()
    }

    private fun pickFromGallery() {
        val galleryIntent =Intent(Intent.ACTION_PICK)
        galleryIntent.type="image/*" //onlyimagepick
        startActivityForResult(
            galleryIntent,
            IMAGE_PICK_GALLERY_CODE
        )
    }

    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(this,storagePermission,STORAGE_REQUEST_CODE)
    }

    private fun checkStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )==PackageManager.PERMISSION_GRANTED
    }

    private fun pickFromCamera(){
        val values=ContentValues()
        values.put(MediaStore.Images.Media.TITLE,"Image Title")
        values.put(MediaStore.Images.Media.DESCRIPTION,"Image Description")
        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(
            cameraIntent,
            IMAGE_PICK_CAMERA_CODE
        )
    }
    private fun requestCameraPermission(){
        ActivityCompat.requestPermissions(this,cameraPermission,CAMERA_REQUEST_CODE)
    }
    private fun checkCameraPermission(): Boolean {
        val result=ContextCompat.checkSelfPermission(this,
        Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED
        val result1=ContextCompat.checkSelfPermission(this,
        Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED
        return result && result1
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            CAMERA_REQUEST_CODE->{
                if(grantResults.isNotEmpty()){
                    val cameraAccepted=grantResults[0]==PackageManager.PERMISSION_GRANTED
                    val storageAccepted=grantResults[1]==PackageManager.PERMISSION_GRANTED
                    if(cameraAccepted && storageAccepted){
                        pickFromCamera()
                    }else{
                        Toast.makeText(this,"Camera and Storage permission are required",Toast.LENGTH_SHORT).show()
                    }
                }
            }
            STORAGE_REQUEST_CODE->{
                if(grantResults.isNotEmpty()){
                    val storageAccepted=grantResults[0]==PackageManager.PERMISSION_GRANTED
                    if(storageAccepted){
                        pickFromGallery()
                    }else{
                        Toast.makeText(this,"Storage permission is required",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode ==Activity.RESULT_OK){
            if(requestCode==IMAGE_PICK_GALLERY_CODE){
                CropImage.activity(data!!.data)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this)
            }else if(requestCode==IMAGE_PICK_CAMERA_CODE){
                CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this)
            }else if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                val result=CropImage.getActivityResult(data)
                if(resultCode ==Activity.RESULT_OK){
                    val resultUri=result.uri
                    imageUri =resultUri
                    pImageView.setImageURI(resultUri)
                    Log.d("ImageUri", "Image URI: $resultUri")
                }
                else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                    val error =result.error
                    Toast.makeText(this,""+error,Toast.LENGTH_SHORT).show()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}