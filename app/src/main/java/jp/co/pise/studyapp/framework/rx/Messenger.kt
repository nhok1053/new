package jp.co.pise.studyapp.framework.rx

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class Messenger {
    private val _bus = PublishSubject.create<IMessage>().toSerialized()

    fun send(message: IMessage) {
        _bus.onNext(message)
    }

    fun <T : IMessage> register(messageClazz: Class<T>): Observable<T> {
        return _bus.ofType(messageClazz)
    }
}
