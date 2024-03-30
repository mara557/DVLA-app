import android.content.Context
import com.mara.dvlavehicleinformation.R
import java.io.InputStream
import java.util.Properties

object ApiKeyProvider {
    fun getApiKey(context: Context): String {
        val properties = Properties()
        val inputStream: InputStream = context.resources.openRawResource(R.raw.config)
        properties.load(inputStream)
        return properties.getProperty("api.key") ?: ""
    }
}
