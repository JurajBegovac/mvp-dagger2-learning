package beg.hr.mvpdagger;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

public class RxBleObservable {
    public static final int BLE_REQUEST_CODE = 113;

    private Subject<Integer, Integer> bleEnableResultSubject = PublishSubject.create();

    private Activity m_activity;

    public RxBleObservable(Activity p_activity) {
        m_activity = p_activity;
    }

    public Observable<Integer> enable() {
        return Observable.defer(() -> {
            BluetoothManager bluetoothManager
                    = (BluetoothManager) m_activity.getSystemService(Context.BLUETOOTH_SERVICE);
            BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
            if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                m_activity.startActivityForResult(enableBtIntent, BLE_REQUEST_CODE);
            } else {
                new Handler().post(() -> bleEnableResultSubject.onNext(Activity.RESULT_OK));
            }
            return bleEnableResultSubject.asObservable();
        }).subscribeOn(AndroidSchedulers.mainThread()).first();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != BLE_REQUEST_CODE) return;
        if (resultCode == Activity.RESULT_OK) bleEnableResultSubject.onNext(Activity.RESULT_OK);
    }
}
