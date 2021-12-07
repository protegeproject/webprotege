package ch.unifr.digits.webprotege.attestation.client;

import ch.unifr.digits.webprotege.attestation.client.jszip.JSZip;
import ch.unifr.digits.webprotege.attestation.client.jszip.ZipObject;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.typedarrays.shared.ArrayBuffer;
import com.google.gwt.xhr.client.XMLHttpRequest;
import edu.stanford.bmir.protege.web.shared.download.DownloadFormatExtension;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import elemental2.promise.Promise;

import static edu.stanford.bmir.protege.web.shared.download.ProjectDownloadConstants.*;

public class ProjectDeflateDownloader {

    private final ProjectId projectId;
    private final RevisionNumber revisionNumber;
    private final DownloadFormatExtension downloadFormatExtension;

    public ProjectDeflateDownloader(ProjectId projectId, RevisionNumber revisionNumber, DownloadFormatExtension downloadFormatExtension) {
        this.projectId = projectId;
        this.revisionNumber = revisionNumber;
        this.downloadFormatExtension = downloadFormatExtension;
    }

    public Promise<String> download() {
        String encodedProjectName = URL.encode(projectId.getId());
        String url = GWT.getHostPageBaseURL() + "download?"
                + PROJECT + "=" + encodedProjectName  +
                "&" + REVISION + "=" + revisionNumber.getValue() +
                "&" + FORMAT + "=" + downloadFormatExtension.getExtension();

        final RegExp regex = RegExp.compile(".*" + downloadFormatExtension.getExtension());
        XMLHttpRequest request = XMLHttpRequest.create();
        request.setResponseType(XMLHttpRequest.ResponseType.ArrayBuffer);

        Promise<String> promise = new Promise<>((resolve, reject) -> {

            request.setOnReadyStateChange(xhr -> {
                if (xhr.getReadyState() != 4) return;
                if (xhr.getStatus() >= 300) {
                    reject.onInvoke(xhr.getStatusText());
                    return;
                }
                ArrayBuffer buffer = xhr.getResponseArrayBuffer();
                JSZip.loadAsync(buffer).then(jszip -> {
                    ZipObject[] files = jszip.file(regex);
                    ZipObject zip = files[0];
                    zip.async("text", null).then(res -> {
                        resolve.onInvoke(res.toString());
                        return null;
                    }).catch_(error -> {
                        reject.onInvoke(error);
                        return null;
                    });
                    return null;
                }).catch_(error -> {
                    reject.onInvoke(error);
                    return null;
                });
            });
        });

        request.open("GET", url);
        request.send();
        return promise;
    }

}
