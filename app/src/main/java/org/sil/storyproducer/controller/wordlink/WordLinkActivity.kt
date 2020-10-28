package org.sil.storyproducer.controller.wordlink

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import org.sil.storyproducer.R
import org.sil.storyproducer.controller.adapter.RecordingsListAdapter
import org.sil.storyproducer.model.Phase
import org.sil.storyproducer.model.PhaseType
import org.sil.storyproducer.model.Workspace
import org.sil.storyproducer.tools.toolbar.PlayBackRecordingToolbar
import java.util.*

class WordLinkActivity : AppCompatActivity(), PlayBackRecordingToolbar.ToolbarMediaListener {

    // private lateinit var recordingToolbar : WordLinkRecordingToolbar
    private lateinit var displayList : RecordingsListAdapter.RecordingsListModal
    lateinit var bottomSheet: ConstraintLayout
    private val wordLinkHistory: Stack<String> = Stack()

    // TODO Refactor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wordlink)
        Workspace.activePhase = Phase(PhaseType.WORDLINK)
        val term = intent.getStringExtra("ClickedTerm")
        Workspace.activeWordLink = Workspace.termToWordLinkMap[Workspace.termFormToTermMap[term.toLowerCase()]]!!
        wordLinkHistory.push(term)

        setupStatusBar()

        setupToolbar(getApplicationContext())

//        setupBottomSheet()
//
//        setupNoteView()
//
//        setupRecordingList()

        // Keeps keyboard from automatically popping up on opening activity
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    private fun setupStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val hsv : FloatArray = floatArrayOf(0.0f,0.0f,0.0f)
            Color.colorToHSV(ContextCompat.getColor(this, Workspace.activePhase.getColor()), hsv)
            hsv[2] *= 0.8f
            window.statusBarColor = Color.HSVToColor(hsv)
        }
    }

    private fun setupToolbar(context: Context){
        val toolbar: androidx.appcompat.widget.Toolbar? = findViewById(R.id.wordlink_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(context,
                Workspace.activePhase.getColor())))
    }

//    private fun setupBottomSheet(){
//        bottomSheet = findViewById(R.id.bottom_sheet)
//
//        from(bottomSheet).isFitToContents = false
//        from(bottomSheet).peekHeight = dpToPx(48, this)
//
//        if(Workspace.activeWordLink.wordLinkRecordings.isNotEmpty()){
//            from(bottomSheet).state = STATE_EXPANDED
//        }
//        else {
//            from(bottomSheet).state = STATE_COLLAPSED
//        }
//    }

//    private fun setupRecordingList(){
//        displayList = RecordingsListAdapter.RecordingsListModal(this, recordingToolbar)
//        displayList.embedList(findViewById(android.R.id.content))
//        displayList.setParentFragment(null)
//        displayList.show()
//    }
//
//    /**
//     * Updates the textViews with the current keyterm information
//     */
//    // TODO Refactor out recording toolbar stuff
//    private fun setupNoteView(){
//        val actionBar = supportActionBar
//
//        actionBar?.title = keytermHistory.peek()
//
//        val keyTermTitleView = findViewById<TextView>(R.id.keyterm_title)
//        var titleText = ""
//        if(Workspace.activeKeyterm.term.toLowerCase() != keytermHistory.peek().toLowerCase()) {
//            titleText = Workspace.activeKeyterm.term
//        }
//        for (termForm in Workspace.activeKeyterm.termForms){
//            if(termForm != keytermHistory.peek()) {
//                if (titleText.isNotEmpty()) {
//                    titleText += " / $termForm"
//                }
//                else{
//                    titleText = termForm
//                }
//            }
//        }
//        if(titleText == ""){
//            keyTermTitleView.visibility = View.GONE
//        }
//        else {
//            keyTermTitleView.visibility = View.VISIBLE
//            keyTermTitleView.text = titleText
//        }
//
//        val explanationView = findViewById<TextView>(R.id.explanation_text)
//        explanationView.text = Workspace.activeKeyterm.explanation
//
//        val relatedTermsView = findViewById<TextView>(R.id.related_terms_text)
//        relatedTermsView.text = Workspace.activeKeyterm.relatedTerms.fold(SpannableStringBuilder()){
//            result, relatedTerm -> result.append(stringToKeytermLink(relatedTerm, this)).append("   ")
//        }
//        relatedTermsView.movementMethod = LinkMovementMethod.getInstance()
//
//        val alternateRenderingsView = findViewById<TextView>(R.id.alternate_renderings_text)
//        alternateRenderingsView.text = Workspace.activeKeyterm.alternateRenderings.fold(""){
//            result, alternateRendering -> "$result\u2022 $alternateRendering\n"
//        }.removeSuffix("\n")
//
//        val bundle = Bundle()
//        bundle.putInt(SLIDE_NUM, 0)
//        recordingToolbar = KeytermRecordingToolbar()
//        recordingToolbar.arguments = bundle
//        supportFragmentManager.beginTransaction().replace(R.id.toolbar_for_recording_toolbar, recordingToolbar).commit()
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.menu_keyterm_view, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.closeKeyterm -> {
//                saveKeyterm()
//                if(intent.hasExtra(PHASE)) {
//                    Workspace.activePhase = Phase(intent.getSerializableExtra(PHASE) as PhaseType)
//                }
//                finish()
//                true
//            }
//            R.id.helpButton -> {
//                helpDialog(this, "${Workspace.activePhase.getPrettyName()} Help").show()
//                true
//            }
//            else -> {
//                onBackPressed()
//                true
//            }
//        }
//    }
//
//    override fun onStoppedToolbarRecording() {
//        val recordingExpandableListView = findViewById<RecyclerView>(R.id.recordings_list)
//        recordingExpandableListView.adapter?.notifyDataSetChanged()
//        if(from(bottomSheet).state == STATE_COLLAPSED) {
//            from(bottomSheet).state = STATE_EXPANDED
//        }
//        recordingExpandableListView.smoothScrollToPosition(0)
//    }
//
//    override fun onStartedToolbarMedia() {
//        displayList.stopAudio()
//    }
//
//    override fun onStartedToolbarRecording() {
//        super.onStartedToolbarRecording()
//        displayList.resetRecordingList()
//    }
//
//    /**
//     * When the back button is pressed, the bottom sheet will close if currently opened or return to
//     * the previous keyterm or close the activity if there is no previous keyterm to return to
//     */
//    override fun onBackPressed() {
//        if( from(bottomSheet).state == STATE_EXPANDED){
//            from(bottomSheet).state = STATE_COLLAPSED
//        }
//        else {
//            saveKeyterm()
//            keytermHistory.pop()
//            if (keytermHistory.isEmpty()) {
//                if(intent.hasExtra(PHASE)) {
//                    Workspace.activePhase = Phase(intent.getSerializableExtra(PHASE) as PhaseType)
//                }
//                super.onBackPressed()
//                finish()
//            } else {
//                Workspace.activeKeyterm = Workspace.termToKeyterm[Workspace.termFormToTerm[keytermHistory.peek().toLowerCase()]]!!
//                setupNoteView()
//                setupRecordingList()
//            }
//        }
//    }
//
    fun replaceActivityWordLink(term: String) {
//        saveWordLink()
//        //Set keyterm from link as active keyterm
//        Workspace.activeKeyterm = Workspace.termToKeyterm[Workspace.termFormToTerm[term.toLowerCase()]]!!
//        //Add new keyterm fragments to stack
//        keytermHistory.push(term)
//        setupNoteView()
//        setupRecordingList()
//        from(bottomSheet).state = STATE_COLLAPSED
    }
//
//    /**
//     * Saves the active keyterm to the workspace and exports an up-to-date json file for all keyterms
//     **/
//    private fun saveWordLink () {
//        Workspace.termToKeyterm[Workspace.activeKeyterm.term] = Workspace.activeKeyterm
//        val keytermList = KeytermList(Workspace.termToKeyterm.values.toList())
//        Thread(Runnable{ let { keytermList.toJson(it) } }).start()
//    }
}