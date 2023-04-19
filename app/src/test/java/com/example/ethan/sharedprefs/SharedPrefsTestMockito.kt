import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.example.ethan.AgentHandler
import com.example.ethan.sharedprefs.SharedPrefs
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations


class SharedPrefsTest {

    @Mock
    private lateinit var activity: Activity

    @Mock
    private lateinit var sharedPrefs: SharedPreferences

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun basic(){

    }

    @Test
    fun testInitSharedPrefs() {
        val mockEditor = Mockito.mock(SharedPreferences.Editor::class.java)
        `when`(activity.getPreferences(Context.MODE_PRIVATE)).thenReturn(sharedPrefs)

        // First time initialization
        `when`(sharedPrefs.contains("initialized")).thenReturn(false)
        `when`(sharedPrefs.edit()).thenReturn(mockEditor)
        `when`(mockEditor.putString("08:00", AgentHandler.goodMorningDialogue.getResTimeID())).thenReturn(mockEditor)
        `when`(mockEditor.putString("12:00", AgentHandler.lunchBreakConsultant.getResTimeID())).thenReturn(mockEditor)
        `when`(mockEditor.putString("15:00", AgentHandler.navigationAssistance.getResTimeID())).thenReturn(mockEditor)
        `when`(mockEditor.putString("18:00", AgentHandler.socialAssistance.getResTimeID())).thenReturn(mockEditor)
        `when`(mockEditor.putString("steam_id", "76561198198615839")).thenReturn(mockEditor)
        `when`(mockEditor.putBoolean("initialized", true)).thenReturn(mockEditor)
        doNothing().`when`(mockEditor).apply()

        SharedPrefs.initSharedPrefs(activity)
        /*
        assertEquals(false, SharedPrefs.sharedPrefs?.getBoolean("initialized", false))

        // Subsequent initialization
        `when`(sharedPrefs.contains("initialized")).thenReturn(true)

        SharedPrefs.initSharedPrefs(activity)

        assertEquals(true, SharedPrefs.sharedPrefs?.getBoolean("initialized", false))
        */

    }
}