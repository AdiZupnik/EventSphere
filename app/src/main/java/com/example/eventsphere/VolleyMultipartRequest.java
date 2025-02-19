package com.example.eventsphere;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

    public class VolleyMultipartRequest extends Request<NetworkResponse> {
        private final String boundary = "===" + System.currentTimeMillis() + "===";
        private final Response.Listener<NetworkResponse> listener;
        private final Response.ErrorListener errorListener;
        private final Map<String, String> params;
        private final Map<String, DataPart> fileParams;

        public VolleyMultipartRequest(int method, String url, Response.Listener<NetworkResponse> listener, Response.ErrorListener errorListener) {
            super(method, url, errorListener);
            this.listener = listener;
            this.errorListener = errorListener;
            this.params = new HashMap<>();
            this.fileParams = new HashMap<>();
        }

        @Override
        public String getBodyContentType() {
            return "multipart/form-data; boundary=" + boundary;
        }

        @Override
        public byte[] getBody() throws AuthFailureError {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bos);

            try {
                // Add parameters
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    buildPart(dos, entry.getKey(), entry.getValue());
                }

                // Add files
                for (Map.Entry<String, DataPart> entry : fileParams.entrySet()) {
                    DataPart dataPart = entry.getValue();
                    buildPart(dos, entry.getKey(), dataPart.getFileName(), dataPart.getContent(), dataPart.getContentType());
                }

                // End multipart form data
                dos.writeBytes("--" + boundary + "--" + "\r\n");
                dos.flush();

                return bos.toByteArray();
            } catch (IOException e) {
                Log.e("VolleyMultipartRequest", "IOException while building multipart request", e);
                return null;
            } finally {
                try {
                    bos.close();
                    dos.close();
                } catch (IOException e) {
                    // Handle close exception
                }
            }
        }

        @Override
        protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
            try {
                String charset = HttpHeaderParser.parseCharset(response.headers, "utf-8");
                return Response.success(
                        response,
                        HttpHeaderParser.parseCacheHeaders(response));
            } catch (Exception e) {
                return Response.error(new VolleyError(e));
            }
        }

        @Override
        protected void deliverResponse(NetworkResponse response) {
            listener.onResponse(response);
        }

        @Override
        public void deliverError(VolleyError error) {
            errorListener.onErrorResponse(error);
        }

        public static class DataPart {
            private String fileName;
            private byte[] content;
            private String contentType;

            public DataPart(String fileName, byte[] content) {
                this.fileName = fileName;
                this.content = content;
                this.contentType = "image/jpeg";
            }

            public DataPart(String fileName, byte[] content, String contentType) {
                this.fileName = fileName;
                this.content = content;
                this.contentType = contentType;
            }

            public String getFileName() {
                return fileName;
            }

            public byte[] getContent() {
                return content;
            }

            public String getContentType() {
                return contentType;
            }
        }

        private void buildPart(DataOutputStream dataOutputStream, String parameterName, String parameterValue) throws IOException {
            dataOutputStream.writeBytes("--" + boundary + "\r\n");
            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + parameterName + "\"" + "\r\n");
            dataOutputStream.writeBytes("Content-Type: text/plain; charset=UTF-8" + "\r\n");
            dataOutputStream.writeBytes("\r\n");
            dataOutputStream.writeBytes(parameterValue + "\r\n");
        }

        private void buildPart(DataOutputStream dataOutputStream, String fieldName, String fileName, byte[] fileContent, String contentType) throws IOException {
            dataOutputStream.writeBytes("--" + boundary + "\r\n");
            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + fieldName + "\"; filename=\"" + fileName + "\"" + "\r\n");
            dataOutputStream.writeBytes("Content-Type: " + contentType + "\r\n");
            dataOutputStream.writeBytes("Content-Transfer-Encoding: binary" + "\r\n");
            dataOutputStream.writeBytes("\r\n");

            ByteArrayInputStream fileInputStream = new ByteArrayInputStream(fileContent);
            int bytesAvailable = fileInputStream.available();

            int maxBufferSize = 1024 * 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[] buffer = new byte[bufferSize];

            // Read file and write it into form...
            int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                dataOutputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            dataOutputStream.writeBytes("\r\n");
        }

        public void addStringParam(String name, String value) {
            this.params.put(name, value);
        }

        public void addFile(String name, String filename, byte[] content) {
            this.fileParams.put(name, new DataPart(filename, content));
        }

        public void addFile(String name, String filename, byte[] content, String contentType) {
            this.fileParams.put(name, new DataPart(filename, content, contentType));
        }
    }
