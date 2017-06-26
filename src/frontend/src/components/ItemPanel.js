import React from 'react';
import { Row, Col } from 'react-bootstrap'
import { FormattedNumber } from 'react-intl'
import ScrollArea  from 'react-scrollbar'

import './ItemPanel.module.css'

const ItemPanel = ({tipos, handler}) => {
    if (!tipos){
      return <ScrollArea className="productScrollView" smoothScrolling={true} contentClassName="productCatalog" horizontal={false} />
    }

    return (
      <ScrollArea className="productScrollView" smoothScrolling={true} contentClassName="productCatalog" horizontal={false}>
        <Col xs={12}>
          {Object.keys(tipos).map((tipo) =>
            <Row key={tipo}>
              {tipos[tipo].map((item) =>
                <CatalogItem key={item.id} itemTipo={tipo} item={item} handler={handler}/>
              )}
            </Row>
          )}
        </Col>
      </ScrollArea>
    )
}

const CatalogItem = ({itemTipo, item, handler}) => {
  return (
    <Col xs={12} sm={6} md={4} lg={2}>
      <div className={'catalogItem item-' + itemTipo} onClick={handler.onVendeItem(item)}>
        <h3>{item.descricao}</h3>
        <h3><FormattedNumber style='currency' currency='BRL' value={item.valor} /></h3>
      </div>
    </Col>
  )
}

export default ItemPanel;
