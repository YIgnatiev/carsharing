package shedule.budivnictvo.com.shedule;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import net.sourceforge.jtds.jdbc.Driver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;




public class AsyncRequest  extends AsyncTask<String ,Void, JSONArray>{


    final static String MSSQL_DB = "jdbc:jtds:sqlserver://testandroid.mssql.somee.com:1433:/testandroid";
    final static String MSSQL_LOGIN = "testandroid";
    final static String MSSQL_PASS = "Qwe123123";

    private Activity context;

    public AsyncRequest (Activity context){
        this.context = context;
    }
    private ProgressDialog dialog;
    @Override
    protected JSONArray doInBackground(String... query) {
        JSONArray  resultSet = new JSONArray();

        try{
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();

            Connection con = null;
            Statement st = null;
            ResultSet rs =null;
            try{
             con = DriverManager.getConnection(MSSQL_DB, MSSQL_LOGIN,MSSQL_PASS);

                if (con != null){
                    st = con.createStatement();
                    rs = st.executeQuery(query[0]);
                    if (rs != null){
                        JSONObject rowObject = null;
                        while(rs.next()){
                                rowObject = new JSONObject();
                                rowObject.put("id_child" , rs.getInt("id_child"));
                                rowObject.put("id_consumer",rs.getInt("id_consumer"));
                                rowObject.put("num",rs.getString("num"));
                                rowObject.put("time_min",rs.getInt("time_min"));
                                rowObject.put("KOD_kurs",rs.getInt("KOD_kurs"));
                                rowObject.put("nazvan_kurs", rs.getString("nazvan_kurs"));
                                rowObject.put("date_urok" , rs.getString("date_urok"));
                                rowObject.put("times_urok" , rs.getString("times_urok"));
                            resultSet.put(rowObject);
                        }
                    }
                }

            }catch (SQLException e ){
                Log.v("test", "SQLException");
               e.printStackTrace();
            }catch (JSONException e){
                e.printStackTrace();

            }finally {
                try{
                    if (rs != null) rs.close();
                    if (st != null)rs.close();
                    if(con != null)con.close();
                }catch (SQLException e){
                   e.printStackTrace();
                }
            }

        }catch(ClassNotFoundException e){
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return resultSet;
    }


    @Override
    protected void onPreExecute() {
        this.dialog = new ProgressDialog(context);
        this.dialog.setMessage(context.getResources().getString(R.string.loading));
       if (!this.dialog.isShowing()) {
            this.dialog.show();
       }
    }

    @Override
    protected void onPostExecute(JSONArray jsonArray) {
        this.dialog.dismiss();
        Intent intent = new Intent(AppContext.BROADCAST_JSON);
        AppContext.setArray(jsonArray);
        context.sendBroadcast(intent);

    }
}

