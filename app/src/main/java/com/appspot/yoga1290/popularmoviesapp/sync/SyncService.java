package com.appspot.yoga1290.popularmoviesapp.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Service;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.appspot.yoga1290.popularmoviesapp.R;
import com.appspot.yoga1290.popularmoviesapp.tasks.FetchMovieReviews;
import com.appspot.yoga1290.popularmoviesapp.tasks.FetchPopularMovies;
import com.appspot.yoga1290.popularmoviesapp.tasks.FetchTopRatedMovies;


public class SyncService extends Service {

    public static final String TASK_NAME = "TASK_NAME";
    public static final String MOVIE_ID = "MOVIE_ID";

    public static void setSyncPeriod(Context context, Bundle syncBundle, int minutes) {

        ContentResolver.addPeriodicSync(
                new Account(
                        context.getResources().getString(R.string.dummy_account_name),
                        context.getResources().getString(R.string.account_type)
                ),
                context.getResources().getString(R.string.contentAuthority),
                syncBundle,
                minutes * 60L * 1000L
        );
    }
    public static void SyncNOW(Context context, Bundle syncBundle)
    {
        Log.i(TAG, "SyncAdapter.SyncNOW:" + syncBundle.getString(SyncService.TASK_NAME));

        Account mAccount;
        // Create the account type and default account
        mAccount = new Account(
                context.getResources().getString(R.string.dummy_account_name),
                context.getResources().getString(R.string.account_type));

        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(
                        ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        Log.i(TAG,"CreateSyncAccount");
        if (accountManager.addAccountExplicitly(mAccount, null, null)) {
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
            System.out.println("CreateSyncAccount/if");
        } else {
            System.out.println("CreateSyncAccount/else");
        }

        Log.i(TAG, "SyncAdapter.SyncNOW:" + syncBundle.getString(SyncService.TASK_NAME));
//        ContentResolver mResolver = context.getContentResolver();
        ContentResolver.setSyncAutomatically(mAccount,
                context.getResources().getString(R.string.contentAuthority)
                , true);
        ContentResolver.requestSync(mAccount,
                context.getResources().getString(R.string.contentAuthority)
                , syncBundle);
    }



    public static final String TAG= SyncService.class.getSimpleName();
    // Storage for an instance of the sync adapter
    private static AbstractThreadedSyncAdapter sSyncAdapter = null;
    // Object to use as a thread-safe lock
    private static final Object sSyncAdapterLock = new Object();
    /*
     * Instantiate the sync adapter object.
     */
    @Override
    public void onCreate() {
        Log.i(TAG, "SyncService.onCreate");
        /*
         * Create the sync adapter as a singleton.
         * Set the sync adapter as syncable
         * Disallow parallel syncs
         */
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null) {

                sSyncAdapter= new AbstractThreadedSyncAdapter(getApplicationContext(),true){

                    @Override
                    public void onPerformSync(
                            Account account,
                            Bundle syncBundle,
                            String authority,
                            ContentProviderClient provider,
                            SyncResult syncResult) {

                        Log.i(TAG, "SyncAdapter.onPerformSync");
                        Log.i(TAG, "SyncAdapter.onPerformSync.bundle:"+ syncBundle.getString(SyncService.TASK_NAME));

                        String movieId = "";
                        String action = getApplicationContext().getResources().getString(R.string.post_sync_broadcast_reciever);
                        try {

                            String task = syncBundle.getString(SyncService.TASK_NAME);


                            if(FetchTopRatedMovies.class.getSimpleName().equals(task)) {

                                FetchTopRatedMovies.sync(getContext());

                            } else if(FetchPopularMovies.class.getSimpleName().equals(task)) {

                                FetchPopularMovies.sync(getContext());

                            } else if(FetchMovieReviews.class.getSimpleName().equals(task)) {

                                movieId = syncBundle.getString(SyncService.MOVIE_ID);
                                FetchMovieReviews.fetchByMovieId(getContext(), movieId);
                                action = getApplicationContext().getResources().getString(R.string.post_review_sync_broadcast_reciever);

                            }



                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e(SyncService.class.getSimpleName(), e.getLocalizedMessage());
                        }
                        Intent intent = new Intent();
                        intent.putExtra(SyncService.MOVIE_ID, movieId);
                        intent.setAction(action);
                        sendBroadcast(intent);

                    }
                };
            }
        }
    }
    /**
     * Return an object that allows the system to invoke
     * the sync adapter.
     *
     */
    @Override
    public IBinder onBind(Intent intent) {
        /*
         * Get the object that allows external processes
         * to call onPerformSync(). The object is created
         * in the base class code when the SyncAdapter
         * constructors call super()
         */
        Log.i(TAG, "SyncService.onBind");

        return sSyncAdapter.getSyncAdapterBinder();
    }

}
