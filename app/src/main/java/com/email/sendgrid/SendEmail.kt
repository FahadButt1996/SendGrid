package com.email.sendgrid

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.github.sendgrid.SendGrid
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class SendEmail : AppCompatActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.send_email -> {
                if (email_from.text.isEmpty()) {
                    showToast("Please enter the Email FROM")

                } else if (email_to.text.isEmpty()) {
                    showToast("Please enter the Email TO")

                } else if (email_subject.text.isEmpty()) {
                    showToast("Please enter the Email Subject")

                } else if (email_body.text.isEmpty()) {
                    showToast("Please enter the Email BODY")

                } else {
                    sendEmail()
                }
            }
        }
    }

    private fun sendEmail() {

        Observable.fromCallable {  SendEmailAsynchronous() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { success ->
                            showToast("Email Semd Successfully")
                        },
                        { error ->
                            showToast("Something wrong happened")
                        }
                )
    }

    private fun SendEmailAsynchronous(): Observable<String> {
        val sendGrid = SendGrid("sendGrid_username", "sendGrid_Password")
        sendGrid.addTo(email_to.text.toString())
        sendGrid.from = email_from.text.toString()
        sendGrid.subject = email_subject.text.toString()
        sendGrid.text = email_body.text.toString()

        val response = sendGrid.send()
        return Observable.just(response)
    }

    private fun showToast(msg: String) {
        Toast.makeText(this@SendEmail, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initClickListeners()
    }

    private fun initClickListeners() {
        send_email.setOnClickListener(this@SendEmail)
    }
}