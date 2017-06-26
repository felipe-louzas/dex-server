import React from 'react';
import { Row, Col, Button, OverlayTrigger, Tooltip } from 'react-bootstrap'
import { FormattedNumber } from 'react-intl'
import ScrollArea  from 'react-scrollbar'
import CupomItem from './CupomItem.js'

import './SalePanel.module.css'

const SalePanel = ({venda, handler, loading}) => {
  if (!venda) {
    venda = {id: 0, lanches: [], valorBruto: 0, valorTotal:0};
  }
  return (
    <div className='salesPanel'>
      <HeaderPanel />
      <ItemList itens={venda.lanches} handler={handler} isLoading={loading} />
      <TotalsPanel subtotal={venda.valorBruto} total={venda.valorTotal} />
      <ButtonsPanel handler={handler} novaVenda={venda.id === 0} isLoading={loading} />
    </div>
  )
};

const HeaderPanel = () => {
  return (
    <Row className='header'>
      <Col xs={12}>
        <Col xs={12}>
          <Col xs={12}>
            <Col xs={7} sm={5} className='align-left'><h3>Item</h3></Col>
            <Col xs={2} className='align-right'><h3>Quant.</h3></Col>
            <Col xsHidden sm={2} className='align-right'><h3>Valor</h3></Col>
            <Col xs={3} className='align-right'><h3>Total</h3></Col>
          </Col>
        </Col>
      </Col>
    </Row>
  );
};

const ItemList = ({itens, handler, isLoading}) => {
  return (
    <Row className='items'>
      <Col xs={12}>
        <ScrollArea className="cupomScrollView" smoothScrolling={true} contentClassName="cupomItemList" horizontal={false}>
          <LoadingOverlay show={isLoading}/>
          {itens.map((item) =>
            <CupomItem key={item.id} item={item} handler={handler} />
          )}
        </ScrollArea>
      </Col>
    </Row>
  );
};

const LoadingOverlay = ({show}) => {
  if (!show) return null;
  return (
    <div className="loading-overlay" />
  )
}

const TotalsPanel = ({subtotal, total}) => {
  return (
    <Row>
      <Col xs={12}>
        <Col xs={12}>
          <Col xs={12}>
            <Col xs={12} className='totais'>
              <Row>
                <Col xs={4} xsOffset={3} className='align-right'><span>Subtotal</span></Col>
                <Col xs={5} className='align-right'><FormattedNumber style='currency' currency='BRL' value={subtotal} /></Col>
              </Row>
              <Row>
                <Col xs={4} xsOffset={3} className='align-right'><span>Descontos</span></Col>
                <Col xs={5} className='align-right'><FormattedNumber style='currency' currency='BRL' value={subtotal-total} /></Col>
              </Row>
              <Row>
                <Col xs={4} xsOffset={3} className='align-right'><span>Total</span></Col>
                <Col xs={5} className='align-right'><FormattedNumber style='currency' currency='BRL' value={total} /></Col>
              </Row>
            </Col>
          </Col>
        </Col>
      </Col>
    </Row>
  );
};

const ButtonsPanel = ({novaVenda, handler, isLoading}) => {
  if (novaVenda){
    return (
      <Row className='botoes'>
        <Col xs={4} xsOffset={7} >
          <Button bsStyle='success' block={true} onClick={handler.onNovaVenda()} disabled={isLoading} ><h5>Nova Venda</h5></Button>
        </Col>
      </Row>
    );
  }else{
    return (
      <Row className='botoes'>
        <Col xs={4}  xsOffset={1}>
          <Button bsStyle='danger' block={true} onClick={handler.onCancelVenda()} disabled={isLoading}><h5>Cancelar</h5></Button>
        </Col>
        <Col xs={4} xsOffset={2} >
          <OverlayTrigger placement="top" overlay={<Tooltip>Não implementado ainda.</Tooltip>}>
            <Button bsStyle='success' block={true} disabled={isLoading} ><h5>Pagamento</h5></Button>
          </OverlayTrigger>
        </Col>
      </Row>
    );
  }
};

export default SalePanel;
