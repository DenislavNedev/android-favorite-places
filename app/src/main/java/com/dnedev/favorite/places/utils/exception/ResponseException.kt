package com.dnedev.favorite.places.utils.exception

import androidx.annotation.StringRes
import java.lang.Exception

class ResponseException(@StringRes val stringResource: Int) : Exception()