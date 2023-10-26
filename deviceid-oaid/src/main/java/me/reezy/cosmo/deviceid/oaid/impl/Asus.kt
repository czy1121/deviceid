 package me.reezy.cosmo.deviceid.oaid.impl

 import android.content.ComponentName
 import android.content.Context
 import android.content.Intent
 import me.reezy.cosmo.deviceid.oaid.OaidProvider
 import me.reezy.cosmo.deviceid.oaid.OaidService
 import me.reezy.cosmo.deviceid.oaid.getId
 import me.reezy.cosmo.deviceid.oaid.hasPackage

 internal class Asus : OaidProvider {
     override fun isSupport(context: Context): Boolean = context.hasPackage("com.asus.msa.SupplementaryDID")

     override fun get(context: Context, callback: (Result<String>) -> Unit) {
         val intent = Intent("com.asus.msa.action.ACCESS_DID")
         intent.component = ComponentName("com.asus.msa.SupplementaryDID", "com.asus.msa.SupplementaryDID.SupplementaryDIDService")

         OaidService.bind(context, intent, callback) {
             it.getId("com.asus.msa.SupplementaryDID.IDidAidlInterface", 1)
         }
    }
}