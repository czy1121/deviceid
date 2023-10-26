package me.reezy.cosmo.deviceid.oaid.impl

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import me.reezy.cosmo.deviceid.oaid.OaidProvider
import me.reezy.cosmo.deviceid.oaid.OaidService
import me.reezy.cosmo.deviceid.oaid.getId
import me.reezy.cosmo.deviceid.oaid.hasPackage
import java.security.MessageDigest


internal class Oppo : OaidProvider {

    override fun isSupport(context: Context): Boolean = context.hasPackage("com.heytap.openid")

    override fun get(context: Context, callback: (Result<String>) -> Unit) {
        val intent = Intent("action.com.heytap.openid.OPEN_ID_SERVICE")
        intent.component = ComponentName("com.heytap.openid", "com.heytap.openid.IdentifyService")

        OaidService.bind(context, intent, callback) {
            it.getId("com.heytap.openid.IOpenID", 1) {
                writeString(context.packageName)
                writeString(context.resolveSign())
                writeString("OUID")
            }
        }
    }

    @SuppressLint("PackageManagerGetSignatures")
    private fun Context.resolveSign(): String {
        val signatures = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES).signatures
        val digest = MessageDigest.getInstance("SHA1").digest(signatures[0].toByteArray())
        val sb = StringBuilder()
        for (b in digest) {
            sb.append(Integer.toHexString((b.toInt() and 255 or 256)).substring(1, 3))
        }
        return sb.toString()
    }
}