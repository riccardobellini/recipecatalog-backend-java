package com.bellini.recipecatalog.exception.publication;

import com.bellini.recipecatalog.model.v1.Publication;

public class DuplicatePublicationException extends RuntimeException {

    private static final long serialVersionUID = -2458702761165927200L;
    private Publication pub;

    public DuplicatePublicationException(Publication pub) {
        super();
        if (pub == null) {
            throw new NullPointerException();
        }
        this.pub = pub;
    }

    @Override
    public String getMessage() {
        return "A record with volume " + pub.getVolume() + " and year " + pub.getYear() + " already exists";
    }
}
