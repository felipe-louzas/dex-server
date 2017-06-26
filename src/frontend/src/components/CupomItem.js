import React from 'react';
import { Row, Col } from 'react-bootstrap'
import { FormattedNumber } from 'react-intl'
import FontAwesome  from 'react-fontawesome'
import './CupomItem.module.css'

const CupomItem = ({item, handler}) => {
    return (
      <div className='product-detail'>
        <Item descricao={item.descricao} valor={item.valorComDescontos} onDelete={handler.onItemDelete(item.id)} />
        {item.ingredientes.map((linha) =>
          <ItemLinha key={linha.ingrediente.id} descricao={linha.ingrediente.descricao} valorUnit={linha.ingrediente.valor} qtd={linha.quantidade} onDelete={handler.onLinhaDelete(item.id, linha.ingrediente.id)} onMinusQtd={handler.onLinhaMinusQtd(item.id, linha.ingrediente.id)} />
        )}
        {item.descontos.map((promo) =>
          <PromocaoLinha key={promo.promocaoId} descricao={promo.descricao} valor={promo.value} />
        )}
      </div>
    )
}

const Item = ({descricao, valor, onDelete}) => {
  return (
    <Row className='descricaoItem'>
      <Col xs={7}>
        <Row className='no-padding'>
          <Col xs={1} className='align-center'><FontAwesome name='times-circle' className='delete' onClick={onDelete}></FontAwesome> </Col>
          <Col xs={11} className='align-left'><span className='descricao'>{descricao}</span></Col>
        </Row>
      </Col>
      <Col xs={5} className='align-right'><FormattedNumber className='subtotal' style='currency' currency='BRL' value={valor} /></Col>
    </Row>
  )
}

const ItemLinha = ({descricao, valorUnit, qtd, onDelete, onMinusQtd}) => {
  return (
    <Row>
      <Col xs={7} sm={5}>
        <Row className='no-padding'>
          <Col xs={2} className='align-center'><FontAwesome name='times-circle' className='delete' onClick={onDelete}/></Col>
          <Col xs={10} className='align-left'><span className="descricaoLinha">{descricao}</span></Col>
        </Row>
      </Col>
      <Col xs={2} className='align-right'>
        <Row className='no-padding'>
          <Col xs={10}><span>{qtd}</span><span>x</span></Col>
          <Col xs={2}>
            <FontAwesome name='minus-circle' className='alterQtd' onClick={onMinusQtd}/>
          </Col>
        </Row>
      </Col>
      <Col xsHidden sm={2} className='align-right'><FormattedNumber style='currency' currency='BRL' value={valorUnit} /></Col>
      <Col xs={3} sm={3} className='align-right'><FormattedNumber style='currency' currency='BRL' value={valorUnit*qtd} /></Col>
    </Row>
  )
}

const PromocaoLinha = ({ descricao, valor }) => {
  return (
    <Row className="promocaoLinha">
      <Col xs={7} sm={5}>
        <Row className='no-padding'>
          <Col xs={10} xsOffset={2} className='align-left'><span>{descricao}</span></Col>
        </Row>
      </Col>
      <Col xs={5} sm={7} className='align-right'><FormattedNumber style='currency' currency='BRL' value={-valor} /></Col>
    </Row>
  )
}

export default CupomItem;
