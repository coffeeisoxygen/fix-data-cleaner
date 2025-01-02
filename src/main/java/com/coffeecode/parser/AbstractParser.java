package com.coffeecode.parser;

import com.coffeecode.exception.CustomException;
import com.coffeecode.logger.GeneralLogging;
import com.coffeecode.logger.TechnicalLogging;
import com.coffeecode.model.DataContainer;

public abstract class AbstractParser implements Parser {
    protected final GeneralLogging logger;
    protected final TechnicalLogging techLogger;
    protected DataContainer container;

    protected AbstractParser() {
        this.logger = new GeneralLogging(this.getClass());
        this.techLogger = new TechnicalLogging(this.getClass());
    }

    @Override
    public void parse(DataContainer container) throws CustomException {
        this.container = container;
        validateContainer();
        parseContent();
    }

    protected abstract void validateContainer() throws CustomException;

    protected abstract void parseContent() throws CustomException;
}
