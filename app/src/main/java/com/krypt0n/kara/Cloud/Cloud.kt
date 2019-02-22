package com.krypt0n.kara.Cloud

import android.annotation.SuppressLint
import android.widget.Toast
import com.krypt0n.kara.Repository.ftpConnected
import com.krypt0n.kara.Repository.ftpPort
import com.krypt0n.kara.Repository.internetAvailable
import com.krypt0n.kara.Repository.ip
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.apache.commons.net.ftp.FTPClient
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

/* Cloud backup that use FTP for remote file upload and download */
object Cloud {
    //user folder on server
    private val ftpFolder = "${System.getProperty("user.name")}/Kara/${Account.name}"
    //ftp client for connecting
    private val client = FTPClient()

    //connect to server via ftp
    @SuppressLint("CheckResult")
    fun connect() {
        if (internetAvailable) {
            try {
                client.connect(ip,ftpPort)
                client.login("francis", "francis")
                ftpConnected = true
            }catch (e : Exception){}
        }
    }
    //check if user folder on server exist
	private fun ftpDirectoryCheck(dirPath : String){
        if (ftpConnected) {
            //go to user folder
            client.changeWorkingDirectory(dirPath)
            //create folder if missing
            if (client.replyCode == 550) {
                client.makeDirectory(ftpFolder)
            }
        }
    }
    //check if backup file on server exist
    private fun backupExist(filePath : String) : Boolean {
        if (ftpConnected) {
            val inputStream = client.retrieveFileStream(filePath)
            if (inputStream == null || client.replyCode == 550) {
                return false
            }
        }
        return true
    }
    //make backup
    fun upload(file : String) {
        try {
            ftpDirectoryCheck(ftpFolder)
            //upload backup to server
            client.storeFile(file, FileInputStream(ftpFolder + file))
        }catch (e : Exception){}
    }
    fun sync(file : String) {
        try {
            //check if backup exist
            if(backupExist(ftpFolder + file)) {
                client.apply {
                    changeWorkingDirectory(ftpFolder)
                    //downloading file to app folder
                    retrieveFile(file,FileOutputStream(ftpFolder + file))
                }
            }
        } catch (e : IOException) {}
    }
}