package com.example.testautofillapp

import android.R
import android.app.assist.AssistStructure
import android.os.Build
import android.os.CancellationSignal
import android.service.autofill.*
import android.view.autofill.AutofillId
import android.view.autofill.AutofillValue
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import org.intellij.lang.annotations.Identifier
import java.lang.Exception
import java.util.*


@RequiresApi(Build.VERSION_CODES.O)
class MyAutofillService: AutofillService() {

    companion object {
        var z: MutableList<AutofillId> = arrayListOf()
        var fieldsIds: FieldsIds? = FieldsIds(null, null)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun parseS(structure: AssistStructure): MyAutofillService.FieldsIds? {


        for(i in 0 until structure.windowNodeCount) {
            var child =  structure.getWindowNodeAt(i)
            println(child.displayId)
            for(i in 0 until child.rootViewNode.childCount) {
                recChild2(child.rootViewNode.getChildAt(i))
            }
        }

        println(x!!.usernameId)

        return x
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun recChild2(child: AssistStructure.ViewNode) {
        println(listOf(
            child.autofillType,
            child.id,
            child.autofillId,
            child.className,
            child.inputType,
            child.idEntry,
            child.hint,
            child.autofillHints?.get(0),
            child.htmlInfo?.attributes
        ))

        if (child.autofillType > 0) {
            if (isEmail(child)) {
                println(listOf("IDENTIFIED: ", child.idEntry, child.autofillType))
                x?.usernameId = child.autofillId
            }
            else if (isPassword(child)) {
                println(listOf("IDENTIFIED: ", child.idEntry, child.autofillType))
                x?.passwordId = child.autofillId
            }
        }

        for(i in 0 until child.childCount) {
            var x = recChild2(child.getChildAt(i))
        }
    }

    override fun onFillRequest(
        request: FillRequest,
        cancellationSignal: CancellationSignal,
        callback: FillCallback
    ) {
        // Get the structure from the request
        val context: List<FillContext> = request.fillContexts
        val structure: AssistStructure = context[context.size - 1].structure

        // Traverse the structure looking for nodes to fill out.
        val parsedStructure: FieldsIds? = parseS(structure)

        // Fetch user data that matches the fields.
        val (username: String, password: String) = listOf("Nastya", "123")

        // Build the presentation of the datasets
        val usernamePresentation = RemoteViews(packageName, R.layout.simple_list_item_1)
        usernamePresentation.setTextViewText(android.R.id.text1, "my_username")
        val passwordPresentation = RemoteViews(packageName, android.R.layout.simple_list_item_1)
        passwordPresentation.setTextViewText(android.R.id.text1, "Password for my_username")

        var x = Dataset.Builder()
        if (parsedStructure != null) {

            if(parsedStructure.usernameId != null)
                x.setValue(
                    parsedStructure.usernameId!!,
                    AutofillValue.forText(username),
                    usernamePresentation
                )

            if(parsedStructure.passwordId != null)
                x.setValue(
                    parsedStructure.passwordId!!,
                    AutofillValue.forText(password),
                    usernamePresentation
                )
        }

        // Add a dataset to the response
        val fillResponse: FillResponse = FillResponse.Builder().addDataset(x.build()).build()

        // If there are no errors, call onSuccess() and pass the response

        callback.onSuccess(fillResponse)

        // callback.onFailure("No matching")
    }

    override fun onSaveRequest(p0: SaveRequest, p1: SaveCallback) {
        TODO("Not yet implemented")
    }

    data class FieldsIds(var usernameId: AutofillId?, var passwordId: AutofillId?)

    data class UserData(var username: String, var password: String)


}