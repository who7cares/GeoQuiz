package com.bignerdranch.geoqiz

data class ButtonState (
    var buttonState: Boolean = true,
    var trueButtonColor: Int = R.color.defaultButton,
    var falseButtonColor: Int = R.color.defaultButton

) {

    companion object {

        fun addButton(size: Int): MutableList<ButtonState> {
            val buttonStates: MutableList<ButtonState> = mutableListOf()
            for (i in 0..size) {
                buttonStates.add(ButtonState())
            }
            return buttonStates
        }

    }

}