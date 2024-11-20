package uz.itteacher.mycontact.model

import uz.itteacher.mycontact.R

enum class CALLTYPE(type: String, icon: Int) {
    INCOMING("Incoming", R.drawable.incoming_call),
    OUTGOING("Outgoing", R.drawable.call_out),
    MISSED("Missed", R.drawable.missed_call)
}