package org.sil.storyproducer.activity

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import androidx.appcompat.app.AppCompatActivity
import org.sil.storyproducer.R
import org.sil.storyproducer.controller.RegistrationActivity
import org.sil.storyproducer.controller.SelectTemplatesFolderController
import org.sil.storyproducer.controller.SelectTemplatesFolderController.Companion.SELECT_TEMPLATES_FOLDER
import org.sil.storyproducer.controller.SelectTemplatesFolderController.Companion.SELECT_TEMPLATES_FOLDER_AND_ADD_DEMO
import org.sil.storyproducer.controller.SelectTemplatesFolderController.Companion.SELECT_TEMPLATES_FOLDER_REQUEST_CODES
import org.sil.storyproducer.controller.SelectTemplatesFolderController.Companion.UPDATE_TEMPLATES_FOLDER
import org.sil.storyproducer.model.Workspace
import org.sil.storyproducer.view.BaseActivityView

open class BaseActivity : AppCompatActivity(), BaseActivityView {

    lateinit var selectTemplatesFolderController: SelectTemplatesFolderController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectTemplatesFolderController = SelectTemplatesFolderController(this, this, Workspace)
    }

    override fun onActivityResult(request: Int, result: Int, data: Intent?) {
        super.onActivityResult(request, result, data)

        if (SELECT_TEMPLATES_FOLDER_REQUEST_CODES.contains(request)) {
            selectTemplatesFolderController.onFolderSelected(request, result, data)
        }
    }

    fun selectTemplatesFolder() {
        selectTemplatesFolderController.openDocumentTree(SELECT_TEMPLATES_FOLDER)
    }

    fun selectTemplatesFolderAndAddDemo() {
        selectTemplatesFolderController.openDocumentTree(SELECT_TEMPLATES_FOLDER_AND_ADD_DEMO)
    }

    fun updateTemplatesFolder() {
        selectTemplatesFolderController.openDocumentTree(UPDATE_TEMPLATES_FOLDER)
    }

    override fun takePersistableUriPermission(uri: Uri) {
        contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        )
    }

    override fun showRegistration() {
        startActivity(Intent(this, RegistrationActivity::class.java))
        finish()
    }

    fun showSelectTemplatesFolderDialog() {
        AlertDialog.Builder(this)
                .setTitle(buildSelectTemplatesTitle())
                .setMessage(buildSelectTemplatesMessage())
                .setPositiveButton(R.string.ok) { _, _ -> updateTemplatesFolder() }
                .setNegativeButton(R.string.cancel, null)
                .create()
                .show()
    }

    private fun buildSelectTemplatesTitle(): Spanned {
        val title = "<b>${getString(R.string.update_workspace)}</b>"
        return if (Build.VERSION.SDK_INT >= 24) {
            Html.fromHtml(title,0)
        } else {
            Html.fromHtml(title) }
    }

    private fun buildSelectTemplatesMessage(): Spanned {
        val message = getString(R.string.workspace_selection_help)
        return if (Build.VERSION.SDK_INT >= 24) {
            Html.fromHtml(message, 0)
        } else {
            Html.fromHtml(message) }
    }

}