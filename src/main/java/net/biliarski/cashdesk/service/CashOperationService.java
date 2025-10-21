package net.biliarski.cashdesk.service;

import net.biliarski.cashdesk.dto.CashOperationRequest;
import net.biliarski.cashdesk.dto.CashOperationResponse;

import java.io.IOException;

public interface CashOperationService {
    CashOperationResponse processCashOperation(CashOperationRequest request) throws IOException;
}
