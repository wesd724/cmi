import { useState } from 'react';
import Tab from '@mui/material/Tab';
import TabContext from '@mui/lab/TabContext';
import TabList from '@mui/lab/TabList';
import TabPanel from '@mui/lab/TabPanel';
import { MARKET, MARKET_NAME } from '../data/constant';
import Comment from './comment';

const CommentTabs = () => {
    const [value, setValue] = useState<string>('1');

    const handleChange = (event: React.SyntheticEvent, newValue: string) => {
        setValue(newValue);
    };

    return (
        <TabContext value={value}>
            <TabList
                variant="scrollable"
                scrollButtons="auto"
                textColor="inherit" TabIndicatorProps={{
                    style: {
                        backgroundColor: "rgb(60, 7, 121)"
                    }
                }} onChange={handleChange}>
                {
                    MARKET.map((v, i) =>
                        <Tab key={i + 1} label={MARKET_NAME[i]} value={String(i + 1)} />
                    )
                }
            </TabList>
            {
                MARKET.map((v, i) =>
                    <TabPanel key={i + 1} sx={{ padding: 0 }} value={String(i + 1)}><Comment market={MARKET[i]} /></TabPanel>
                )
            }
        </TabContext>
    );
}

export default CommentTabs;